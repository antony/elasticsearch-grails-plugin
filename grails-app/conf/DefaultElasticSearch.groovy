/**
 * This {@link groovy.util.ConfigObject} script provides the default plugin configuration.
 *
 * Note : The plugin loaded this file itself and merge it with the GrailsApplication#Config script.
 * Any properties set in the Config.groovy script of your application override those set here.
 * It is not required to add a reference to this file in your Config.groovy
 */

elasticSearch {
  /**
   * Date formats used by the unmarshaller of the JSON responses
   */
  date.formats = ["yyyy-MM-dd'T'HH:mm:ss'Z'"]

  /**
   * Hosts for remote ElasticSearch instances.
   * Will only be used with the "transport" client mode.
   * If the client mode is set to "transport" and no hosts are defined, ["localhost", 9300] will be used by default.
   */
  client.hosts = [
          [host:'localhost', port:9300]
  ]

  /**
   * Default mapping property exclusions
   *
   * No properties matching the given names will be mapped by default
   * ie, when using "searchable = true"
   *
   * This does not apply for classes using mapping by closure
   */
  defaultExcludedProperties = ["password"]
}

environments {
  development {
    /**
     * Possible values : "local", "node", "transport"
     * If set to null, "node" mode is used by default.
     */
    elasticSearch.client.mode = 'local'
  }
  test {
    elasticSearch {
        client.mode = 'local'
        index.store.type = 'memory'
    }
  }
  production {
    elasticSearch.client.mode = 'node'
  }
}