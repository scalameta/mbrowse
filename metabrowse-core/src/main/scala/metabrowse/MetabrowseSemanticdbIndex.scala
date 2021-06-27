package metabrowse

import scala.concurrent.Future
import metabrowse.{schema => d}
import scala.meta.{inputs => m}
import scala.meta.internal.{semanticdb => s}
import MetabrowseEnrichments._
import scala.meta.internal.semanticdb.Scala._

/** Index to lookup symbol definitions and references. */
trait MetabrowseSemanticdbIndex {
  def document: s.TextDocument
  def symbol(sym: String): Future[Option[d.SymbolIndex]]
  def semanticdb(sym: String): Future[Option[s.TextDocument]]
  def dispatch(event: MetabrowseEvent): Unit

  def definition(symbol: String): Option[d.Position] =
    document.occurrences.collectFirst {
      case s.SymbolOccurrence(
            Some(r),
            `symbol`,
            s.SymbolOccurrence.Role.DEFINITION
          ) =>
        r.toPosition(document.uri)
    }

  def resolve(offset: Int): Option[s.SymbolOccurrence] = {
    val input = m.Input.VirtualFile(document.uri, document.text)
    val mpos = m.Position.Range(input, offset, offset)
    val line = mpos.startLine
    val character = mpos.startColumn
    // TODO(olafur) binary search.
    document.occurrences.collectFirst {
      case name @ s.SymbolOccurrence(Some(pos), _, _)
          if pos.startLine <= line &&
            pos.startCharacter <= character &&
            line <= pos.endLine &&
            character <= pos.endCharacter =>
        name
    }
  }

  def fetchSymbol(offset: Int): Future[Option[d.SymbolIndex]] =
    resolve(offset).fold(Future.successful(Option.empty[d.SymbolIndex])) {
      case s.SymbolOccurrence(_, sym, _) =>
        if (sym.isGlobal) {
          symbol(sym)
        } else if (sym.isLocal) {
          // Resolve from local open document.
          val names = document.occurrences.filter(_.symbol == sym)
          val definition = names.collectFirst {
            case s.SymbolOccurrence(
                  Some(r),
                  _,
                  s.SymbolOccurrence.Role.DEFINITION
                ) =>
              r.toPosition(document.uri)
          }
          val references = Map(
            document.uri -> d.Ranges(
              names.collect {
                case s.SymbolOccurrence(
                      Some(r),
                      _,
                      s.SymbolOccurrence.Role.REFERENCE
                    ) =>
                  r.toDocRange
              }
            )
          )
          val dsymbol = d.SymbolIndex(sym, definition, references)
          Future.successful(Some(dsymbol))
        } else {
          Future.successful(None)
        }
    }
}
