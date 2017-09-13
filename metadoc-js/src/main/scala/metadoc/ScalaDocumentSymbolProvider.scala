package metadoc

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import metadoc.schema.Index
import monaco.CancellationToken
import monaco.editor.IReadOnlyModel
import monaco.languages.DocumentSymbolProvider
import monaco.languages.SymbolInformation
import monaco.languages.SymbolKind
import org.langmeta.Denotation
import org.langmeta.semanticdb.ResolvedSymbol
import org.{langmeta => m}

class ScalaDocumentSymbolProvider(index: Index)
    extends DocumentSymbolProvider {
  override def provideDocumentSymbols(
      model: IReadOnlyModel,
      token: CancellationToken
  ) = {
    for {
      doc <- MetadocAttributeService.fetchDocument(model.uri.path)
    } yield {
      val denotations = doc.symbols.map {
        case ResolvedSymbol(s, d) => s -> d
      }.toMap
      val symbols = for {
        sym <- index.symbols
        denotation <- denotations.get(m.Symbol(sym.symbol))
        kind <- symbolKind(denotation)
      } yield {
        new SymbolInformation(
          name = denotation.name,
          containerName = denotation.signature,
          kind = kind,
          location = model.resolveLocation(sym.definition.get)
        )
      }
      js.Array[SymbolInformation](symbols: _*)
    }
  }.toMonacoThenable

  def symbolKind(denotation: Denotation): Option[SymbolKind] = {
    import denotation._

    if (isParam || isTypeParam)
      None
    else if (isVal || isVar)
      Some(SymbolKind.Variable)
    else if (isDef)
      Some(SymbolKind.Function)
    else if (isPrimaryCtor || isSecondaryCtor)
      Some(SymbolKind.Constructor)
    else if (isClass)
      Some(SymbolKind.Class)
    else if (isObject)
      Some(SymbolKind.Object)
    else if (isTrait)
      Some(SymbolKind.Interface)
    else if (isPackage || isPackageObject)
      Some(SymbolKind.Package)
    else if (isType)
      Some(SymbolKind.Namespace) // Note: no type related symbol kind exists
    else
      None
  }
}
