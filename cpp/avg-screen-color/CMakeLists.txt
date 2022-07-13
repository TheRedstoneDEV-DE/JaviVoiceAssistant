cmake_minimum_required(VERSION 2.8)
set (CMAKE_CXX_STANDARD 14)
project( avgColorOfScreen )

find_package(dlib REQUIRED)
#include_directories(${dlib_INCLUDE_DIRS})
#target_link_libraries(your_app dlib::dlib)

include_directories(/usr/lib/jvm/java-16-openjdk-amd64/include/ /usr/lib/jvm/java-16-openjdk-amd64/include/linux/)

find_package( OpenCV REQUIRED )

file(GLOB SRC *.cpp *.h)
#add_executable( avgColorOfScreen ${SRC})
add_library( avgColorOfScreenLib ${SRC} )
#target_link_libraries( avgColorOfScreen dlib::dlib ${OpenCV_LIBS}) 
target_link_libraries( avgColorOfScreenLib dlib::dlib ${OpenCV_LIBS}) 
