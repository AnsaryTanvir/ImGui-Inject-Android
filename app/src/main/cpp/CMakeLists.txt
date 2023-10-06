cmake_minimum_required(VERSION 3.18.1)
#set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -fvisibility=hidden -Wl,--exclude-libs,ALL")

#------------------- BEGIN Payload (Server)  ------------------------#
project("Server")
add_library(

        # Sets the name of the library ---> libServer.so
        Server

        # Set the type of the libray
        SHARED

        # Source file(s).
        Server/Server.cpp
        Server/Offsets.h

        # Utility
        Utils/Utils.h

        # ImGUI
        Server/ImGui/imconfig.h
        Server/ImGui/imgui.cpp
        Server/ImGui/imgui.h
        Server/ImGui/imgui_demo.cpp
        Server/ImGui/imgui_draw.cpp
        Server/ImGui/imgui_tables.cpp
        Server/ImGui/imgui_widgets.cpp
        Server/ImGui/imstb_rectpack.h
        Server/ImGui/imstb_textedit.h
        Server/ImGui/imstb_truetype.h

        Server/ImGui/backends/android_native_app_glue.h
        Server/ImGui/backends/imgui_impl_android.cpp
        Server/ImGui/backends/imgui_impl_android.h
        Server/ImGui/backends/imgui_impl_opengl3.cpp
        Server/ImGui/backends/imgui_impl_opengl3.h
        Server/ImGui/backends/imgui_impl_opengl3_loader.h

        Server/ESP.h
        Server/Roboto-Regular.h
)


# Define and configure the Dobby library as an imported static library
add_library(dobby STATIC IMPORTED)

# Set the path to the Dobby library based on the current ABI (ANDROID_ABI)
set_target_properties(dobby PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/Dobby/${ANDROID_ABI}/libdobby.a)

# Link the Server library with the Dobby static library and log library.
target_link_libraries(Server PRIVATE dobby log android z EGL GLESv3 GLESv2)

#------------------- END Payload ------------------------#


#------------------- BEGIN client ------------------------#
project("Client")
add_library(
        # Sets the name of the library ---> libClient.so
        Client

        # Set the type of the libray
        SHARED

        # Source file(s).
        Client/client.cpp
)
target_link_libraries( Client log)
#------------------- END client ------------------------#

