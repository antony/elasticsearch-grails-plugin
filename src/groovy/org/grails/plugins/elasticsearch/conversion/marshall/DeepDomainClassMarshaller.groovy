package org.grails.plugins.elasticsearch.conversion.marshall

import org.codehaus.groovy.grails.commons.GrailsDomainClassProperty
import org.codehaus.groovy.grails.commons.DomainClassArtefactHandler
import org.grails.plugins.elasticsearch.util.DomainClassRegistry
import org.codehaus.groovy.grails.commons.GrailsDomainClass

class DeepDomainClassMarshaller extends DefaultMarshaller {
  protected doMarshall(instance) {
    def marshallResult = [id: instance.id, 'class': instance.class?.name]
    GrailsDomainClass domainClass = DomainClassRegistry.getDomainClass(instance)
    def mappingProperties = elasticSearchContextHolder.getMappingContext(domainClass)?.propertiesMapping
    for (GrailsDomainClassProperty prop in domainClass.persistentProperties) {
      if(!(prop.name in mappingProperties*.propertyName)){
        continue
      }
      def propertyClassName = instance."${prop.name}"?.class?.name
      def propertyClass = instance."${prop.name}"?.class
      def propertyValue = instance."${prop.name}"

      // Domain marshalling
      if (DomainClassArtefactHandler.isDomainClass(propertyClass)) {
        if (propertyValue.class.searchable) {
          marshallingContext.lastParentPropertyName = prop.name
          marshallResult += [(prop.name): ([id: instance.id, 'class': propertyClassName] + this.marshall(propertyValue))]
        } else {
          marshallResult += [(prop.name): [id: instance.id, 'class': propertyClassName]]
        }

        // Non-domain marshalling
      } else {
        marshallingContext.lastParentPropertyName = prop.name
        marshallResult += [(prop.name): marshallingContext.delegateMarshalling(propertyValue)]
      }
    }
    return marshallResult
  }

  protected nullValue(){
    return []
  }

}
