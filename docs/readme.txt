I'm still porting to this repository, mostly from Struts 1 to Struts 2. Currently the application
here as-is only shares 'public' photos (which is something that can be done better & more easily using 
just Apache or Google Photos) so...it's not yet useful.

That being said here are current rough notes outlining how to use it now. 

CAUTION: Currently ALL photos on the application are viewable by ANYone who can browse to it.

install Linux (it's not targeted at and probably won't work as-is on other platforms)
    can be on old small otherwise useless laptop, or as virtual machine, or ...
install ImageMagick (will need to use "convert" while importing photos to db)
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
    	    manually run src/main/java add.ImportFromFolder class (I have no easier solution yet)
    	        if you've imported the repository as a project in an IDE simply run it from there
    	        otherwise in terminal first extract the war file and then run it, e.g:
    	            mkdir temp4familyPhotos
    	            mv familyPhotos.war temp4familyPhotos
    	            cd temp4familyPhotos
    	            jar -xvf familyPhotos.war
    	            cd WEB-INF
    	            java -classpath "lib/*:classes/." add.ImportFromFolder 
    	        Note: it will connect to db using credentials in ApplicationResources.properties
    	        so if you've changed credentials listed above then also change them in properties
    	        Note: photos can be of any type that normally has extension jpg,gif,png,psd,tif,bmp
set up public route
    set up port forwarding on router
    optionally set up stable address
    optionally set up to require https
deploy war to Tomcat
let someone know route to view photos 
and again, 
    CAUTION: Currently ALL photos on the application are viewable by ANYone who can browse to it.

