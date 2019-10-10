cmake_minimum_required(VERSION 3.15)
project(main1)

set(CMAKE_CXX_STANDARD 14)


include_directories(.)
include_directories(/usr/local/include)


add_executable(main1
        main1.cpp)

set( GLFW_BUILD_DOCS OFF CACHE BOOL "" FORCE )
set( GLFW_BUILD_TESTS OFF CACHE BOOL "" FORCE )
set( GLFW_BUILD_EXAMPLES OFF CACHE BOOL "" FORCE )

find_path(GLEW_INCLUDE_DIR GL/glew.h)
find_package(GLEW 2.1.0 REQUIRED)
find_package(glfw3 3.3 REQUIRED)

target_include_directories(main1 PUBLIC ${OPENGL_INCLUDE_DIR})
target_link_libraries(main1 glfw GLEW::GLEW ${OPENGL_gl_LIBRARY})
