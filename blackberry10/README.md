# MOFILER SDK FOR BLACKBERRY 10 (Cascades):

Check the HelloMofiler project to see rapidly how to use the Mofiler SDK.


## How to include the library in your Cascades project

Read the following article

http://supportforums.blackberry.com/t5/Native-Development-Knowledge/How-to-use-a-third-party-shared-or-static-library-in-a-Cascades/ta-p/1886279

## Quick stuff to get you going

 

    In your app's .project you need to locate <projects></projects> and add your library as <project>libraryname</project> in there.
    Then in your app's .pro file
        add a LIBS += -llibraryname
        add relative path to your library's headers using INCLUDEPATH+=
        in the relevant CONFIG(release, debug|release) sections, add LIBS += -Lrealtivepathtobinary
    (If you add a shared library you need to add it to the BAR file as well)



## LICENSING NOTICE
This library takes some of the code/config values found in the template made by Isaac Gordezky, uploaded to Blackberry Cascades community samples a this URL in github:
https://github.com/blackberry/Cascades-Community-Samples/tree/master/Cascades-Library-Template