dataSource {
    boolean pooling = true
    dbCreate = "update" // one of 'create', 'create-drop','update'
    url = "jdbc:hsqldb:mem:testDb"
//    url = "jdbc:hsqldb:file:grailscrowdDB;shutdown=true"
    driverClassName = "org.hsqldb.jdbcDriver"
    username = "sa"
    password = ""
}

hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    cache.provider_class = 'com.opensymphony.oscache.hibernate.OSCacheProvider'
}

// environment specific settings
environments {
    development {
        dataSource {
            boolean pooling = true
            dbCreate = "create-drop"
            url = "jdbc:mysql://localhost/crowdmail"
            driverClassName = "com.mysql.jdbc.Driver"
            username = "crowd"
            password = "x"
        }
    }

    mem_development {
		dataSource {
			dbCreate = "create-drop" // one of 'create', 'create-drop','update'
			url = "jdbc:hsqldb:mem:devDB"
            //url = "jdbc:hsqldb:file:grailscrowdDB;shutdown=true"
        }
	}


    mem_development {
		dataSource {
			dbCreate = "create-drop" // one of 'create', 'create-drop','update'
			//url = "jdbc:hsqldb:mem:devDB"
            //url = "jdbc:hsqldb:file:grailscrowdDB;shutdown=true"
        }
	}

    dimaDevelopment {
        dataSource {
            boolean pooling = true
            dbCreate = "update"
            url = "jdbc:mysql://localhost/grailscrowd_dev"
            driverClassName = "com.mysql.jdbc.Driver"
            username = "gc"
            password = "gc2008"
        }
    }
    staging {
        dataSource {
            boolean pooling = true
            dbCreate = "update"
            url = "jdbc:mysql://localhost/grailscrowd_staging"
            driverClassName = "com.mysql.jdbc.Driver"
            username = "gcrowd_staging"
            password = "crowd#pa55"
        }
    }
    production {
        dataSource {
            boolean pooling = true
            dbCreate = "update"
            jndiName = "java:comp/env/jdbc/morph-ds"
//            dialect = "org.hibernate.dialect.MySQL5Dialect"
        }
    }
    test {
        dataSource {
            dbCreate = "update"
            url = "jdbc:hsqldb:mem:testDb"
        }
    }
}
