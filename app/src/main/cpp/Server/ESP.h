#ifndef ImGuiAndroid_ESP
#define ImGuiAndroid_ESP

#include "ImGui/imgui_internal.h"

namespace ESP{

    void DrawLine(ImVec2 start, ImVec2 end, ImVec4 color)
    {
        auto background = ImGui::GetBackgroundDrawList();
        if(background){
            background->AddLine(start, end, ImColor(color.x, color.y, color.z, color.w));
        }
    }

    void DrawCircle(float X, float Y, float radius, bool filled, ImVec4 color){

        auto background = ImGui::GetBackgroundDrawList();
        if( background ){

            if( filled ){
                background->AddCircleFilled(ImVec2(X, Y), radius, ImColor(color.x, color.y, color.z, color.w));
            }else{
                background->AddCircle(ImVec2(X, Y), radius, ImColor(color.x, color.y, color.z, color.w));
            }
        }
    }

    void DrawText(float fontSize, ImVec2 position, ImVec4 color, const char *text){

        auto background = ImGui::GetBackgroundDrawList();
        if( background ){
            background->AddText(NULL, fontSize, position, ImColor(color.x, color.y, color.z, color.w), text);
        }
    }
}

#endif