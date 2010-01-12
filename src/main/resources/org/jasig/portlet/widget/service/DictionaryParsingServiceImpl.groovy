import java.io.InputStream
import groovy.xml.StreamingMarkupBuilder
import org.jasig.portlet.widget.service.IDictionaryParsingService

class DictionaryParsingServiceImpl implements IDictionaryParsingService {

    String getDefinitionFromXml(InputStream xml) {
        def response = new XmlSlurper().parse(xml)
        return response.Definitions.Definition.WordDefinition
    }

}