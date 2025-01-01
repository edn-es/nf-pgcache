# Nextflow PostgreSQL cache

Nextflow's PostgreSQL Cache Plugin enables the storage of task execution metadata in a PostgreSQL database, ensuring data persistence and scalability. This plugin is particularly useful for workflows that require robust, centralized caching mechanisms across distributed environments.

Add the plugin to your `nextflow.config` , configure it to connect with a postgre database and ... this is all.
The plugin will create required tables and will store tasks context when you run your pipeline

Among a way to *Centralized Cache Storage*, using this plugin, you're able to see log executions in real time

## License

This plugin is licensed under the MIT License.

## Contributing

Contributions are welcome! Please submit issues or pull requests to the project's GitHub repository.

## Contact

For commercial support, contact the EDN team at contacto@edn.es or refer to the plugin documentation at
https://edn-es.github.io/ng-pgcache/index.html
