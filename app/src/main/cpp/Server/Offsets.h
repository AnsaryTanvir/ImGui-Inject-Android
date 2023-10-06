#ifndef OFFSETS_H
#define OFFSETS_H

// Temple Run, 32 Bit, Version 1.23.5 (66)

// public class CharacterPlayer : CommonPlayer
namespace CharacterPlayer {

    // RVA: 0x780714 Offset: 0x780714 VA: 0x780714 Slot: 4
    // public override void Update() { }
    uintptr_t Update = 0x780714;


    // RVA: 0x7826EC Offset: 0x7826EC VA: 0x7826EC
    // public void StartInvcibility(float duration) { }
    uintptr_t StartInvcibility = 0x7826EC;

}


namespace GameController{

    // RVA: 0x936B70 Offset: 0x936B70 VA: 0x936B70
    // private void HandleCollisionWithObstacles() { }
    uintptr_t HandleCollisionWithObstacles = 0x936B70;

    // RVA: 0x9385DC Offset: 0x9385DC VA: 0x9385DC
    // public void BoostPlayer(float distance, bool isMega = False) { }

    // RVA: 0x93CF34 Offset: 0x93CF34 VA: 0x93CF34
    // public void UseAngelWings() { }

    // RVA: 0x93D198 Offset: 0x93D198 VA: 0x93D198
    // public void UsePermaWings() { }

    // RVA: 0x93D3BC Offset: 0x93D3BC VA: 0x93D3BC
    // public void UseHeadStart() { }

    // RVA: 0x93D5E4 Offset: 0x93D5E4 VA: 0x93D5E4
    // public void UseHeadStartMega() { }


}

#endif OFFSETS_H
