##Introduction

  XMemcached is a high performance, easy to use blocking multithreaded memcached client in java.
  It's nio based and was carefully turned to get top performance.

* [Homepage](https://github.com/killme2008/xmemcached)
* [Downloads](https://github.com/killme2008/xmemcached/tags)
* [Wiki](http://code.google.com/p/xmemcached/w/list)
* [Javadoc](http://fnil.net/docs/xmemcached/index.html)

##News and downloads

 * [Xmemcached 2.0.0 released](https://github.com/killme2008/xmemcached/releases/tag/xmemcached-2.0.0).
 
 Maven dependency:
 
 ```
  <dependency>
      <groupId>com.googlecode.xmemcached</groupId>
      <artifactId>xmemcached</artifactId>
      <version>{version}</version>
 </dependency>
 ```

##Highlights

* Supports all memcached text based protocols and binary protocols(Binary protocol supports since version 1.2.0).
* Supports distributed memcached with standard hash or consistent hash strategy
* Supports for JMX to allow you to monitor and control the behavior of the XMemcachedClient.Change the optimizer's factor or add/remove memcached server dynamically
* Supports weighted server.
* Supports connection pool.You can create more connections to one memcached server with java nio.(since version 1.2.0)
* Supports failure mode and standby nodes.
* Supports integrating to spring framework and hibernate-memcached.
* High performance.
* Supports talking with kestrel(a MQ written in scala) and TokyoTyrant


##FAQ

###How to build project by maven?

      Type command "mvn -Dtest -DfailIfNoTests=false assembly:assembly" to build the project.Maven will download the dependencies automacly and build project.


###How to run unit tests?

      The test.properties file under the src/test/resources folder is used for setting memcached test server.
      Please set test.memcached.servers property,Then run the AllTests class with jvm option "-ea".

###Is Xmemcached compatible with jdk5?

     Yes,since 1.2.0-RC1,Xmemcached is compatible with jdk5.


##Example

        //New a XMemcachedClient instance
         XMemcachedClientBuilder builder = new XMemcachedClientBuilder(AddrUtil.getAddresses("localhost:11211"));
         XMemcachedClient client=builder.build();

        //If you want to use binary protocol
         XMemcachedClientBuilder builder = new XMemcachedClientBuilder(AddrUtil.getAddresses("localhost:11211"));
         builder.setCommandFactory(new BinaryCommandFactory());
         XMemcachedClient client=builder.build();

        //If you want to use xmemcached talking with kestrel
        XMemcachedClientBuilder builder = new XMemcachedClientBuilder(AddrUtil.getAddresses("localhost:11211"));
        builder.setCommandFactory(new KestrelCommandFactory());
        XMemcachedClient client=builder.build();

        //If you want to store primitive type as String
        client.setPrimitiveAsString(true);

        //Add or remove memcached server dynamically
        client.addServer("localhost:12001 localhost:12002");
        client.removeServer("localhost:12001 localhost:12002");


        //get operation
        String name =client.get("test");

        //set add replace append prepend gets
        client.add("hello", 0, "dennis");
        client.replace("hello", 0, "dennis");
        client.append("hello", 0, " good");
        client.prepend("hello", 0, "hello ");
        GetsResponse response=client.gets("hello");
        long cas=response.getCas();
        Obejct value=response.getValue();

        //incr decr
        client.set("a",0,"1");
        client.incr("a",4);
        client.decr("a",4);

        //cas
        client.cas("a", 0, new CASOperation() {
                        @Override
                        public int getMaxTries() {
                                    return 1;  //max try times
                        }
                        @Override
                        public Object getNewValue(long currentCAS, Object currentValue) {
                                    System.out.println("current value " + currentValue);
                                    return 3; //return new value to update
                        }
        });

        //flush_all
        client.flushAll();

        //stats
        Map<InetSocketAddress,Map<String,String>> result=client.getStats();

        // get server versions
        Map<InetSocketAddress,String> version=memcached.getVersions();

        //bulk get
        List<String> keys = new ArrayList<String>();
        keys.add("hello");
        keys.add("test");
        Map<String, Object> map = client.get(keys);

##Enable jmx support

         java -Dxmemcached.jmx.enable=true [YourApp]

Access MBean through

        service:jmx:rmi:///jndi/rmi://[host]:7077/xmemcachedServer

##Integrate to spring framework

             <bean name="memcachedClient"
                    class="net.rubyeye.xmemcached.utils.XMemcachedClientFactoryBean">
                            <property name="servers">
                                      <value>localhost:12000 localhost:12001</value>
                            </property>
             </bean>

##Set server's weight

      //set weight to 2
      client.addServer("localhost",12000,2);

      //or through XMemcachedClientBuilder,pass a weight array to XMemcachedClientBuilder constructor
      MemcachedClientBuilder builder = new XMemcachedClientBuilder(AddrUtil.getAddresses("localhost:12000 localhost:12001"),new int[]{1,3});
      builder.setSessionLocator(new KetamaMemcachedSessionLocator());
      MemcachedClient memcachedClient=builder.build();


More information see [wiki pages](http://code.google.com/p/xmemcached/w/list) please.

## Contributors

* [cnscud](https://code.google.com/u/cnscud/)
* [wolfg1969](https://code.google.com/u/wolfg1969/)
* [vadimp](https://github.com/vadimp)
* [ilkinulas](https://github.com/ilkinulas)
* [aravind](https://github.com/aravind)
* [bmahe](https://github.com/bmahe)
* [jovanchohan](https://github.com/jovanchohan)
* [profondometer](https://github.com/profondometer)
* [machao9email](https://code.google.com/u/100914576372416966057)
* [spudone](https://github.com/spudone)
* [MikeBily](https://github.com/MikeBily)

##License
[Apache License Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)
