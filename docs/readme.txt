current rougl steps to just use this web application to share photos 

CAUTION: Currently ALL photos on the application are viewable by ANYone who can browse to it.

install Linux (it's not targeted at and probably won't work as-is on other platforms)
    can be on old small otherwise useless laptop, or as virtual machine, or ...
install Java (JDK)
install Maven (or download war file from a release, once there is one)
    clone repository
    produce war file
        cd familyPhotos_Java_web
        mvn package
install Tomcat or other web application server
    optionally also install Apache and set stringent security options (not covered here)
install PostgreSQL (not targeted at and probably won't work as-is on other databases)
set up database
    change pg_hba.conf from peer to md5
    as postgres: 
        create user familyphotos_admin with encrypted password 'changeMe';
        create database familyphotos owner familyphotos_admin;
    as normal user add tables,indexes,photos etc: 
    	psql -f familyphotos.sql familyphotos familyphotos_admin --password
    	add photos
    	    put photos in folder ~/Pictures/4import2familyPhotos/
    	    manually run src/main/java/add/ImportFromFolder
set up public route
    set up port forwarding on router
    optionally set up stable address
deploy war to Tomcat
let someone know route to view photos 
and again, 
    CAUTION: Currently ALL photos on the application are viewable by ANYone who can browse to it.

