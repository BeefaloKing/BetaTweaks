//Straight outta 1.3
//Used in mod_oldCustomLogo

package net.minecraft.src;

import java.util.Random;

// Referenced classes of package net.minecraft.src:
//            GuiMainMenu

class LogoEffectRandomizer
{

    public LogoEffectRandomizer(GuiMainMenuCustom guiOldCustomMainMenu, int i, int j)
    {
        mainMenu = guiOldCustomMainMenu;
        field_1312_a = field_1311_b = (double)(10 + j) + GuiMainMenuCustom.getRand().nextDouble() * 32D + (double)i;
    }

    public void func_875_a()
    {
        field_1311_b = field_1312_a;
        if(field_1312_a > 0.0D)
        {
            field_1314_c -= 0.59999999999999998D;
        }
        field_1312_a += field_1314_c;
        field_1314_c *= 0.90000000000000002D;
        if(field_1312_a < 0.0D)
        {
            field_1312_a = 0.0D;
            field_1314_c = 0.0D;
        }
    }

    public double field_1312_a;
    public double field_1311_b;
    public double field_1314_c;
    final GuiMainMenuCustom mainMenu;
}