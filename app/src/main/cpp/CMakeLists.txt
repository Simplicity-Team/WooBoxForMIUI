cmake_minimum_required(VERSION 3.18.1)


set(CMAKE_CXX_STANDARD 17)
set(CMAKE_C_STANDARD 11)

add_subdirectory(dex_builder)

add_library(
        DexBuilder
        SHARED
        native.cpp
        biliroaming.cc
        )
target_include_directories(DexBuilder PUBLIC dex_builder/include)

find_library(
        log-lib
        log)

target_link_libraries(
        DexBuilder
        ${log-lib}
        dex_builder_static
        )