cmake_minimum_required(VERSION 3.26)
project(c_demo C)

set(CMAKE_C_STANDARD 99)

add_executable(c_demo main.c
        chapter01/01_Hello.c)

add_executable(01_Hello chapter01/01_Hello.c
        chapter01/02_CommentDest.c
        chapter01/03_HelloC.c
        chapter01/04_Print.c)
add_executable(main main.c
        chapter01/03_HelloC.c
        chapter01/04_Print.c)
add_executable(02_CommentDest chapter01/02_CommentDest.c)
add_executable(04_Print chapter01/04_Print.c)