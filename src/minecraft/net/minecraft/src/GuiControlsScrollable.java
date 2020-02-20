// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;

// Referenced classes of package net.minecraft.src:
//			GuiScreen, StringTranslate, GameSettings, GuiSmallButton, 
//			GuiButton, KeyBinding

public class GuiControlsScrollable extends GuiScreen
{
	private GuiScreen parentScreen;
	private String screenTitle;
	private GameSettings options;
	
	private int right;
	private int bottom;
	
	private final int left;
	private final int top;
	private final int slotHeight;
	
	private float scrollMultiplier;
	private int selectedButton = -1;
	private boolean drag;
	private float dragY = 0;
	private float amountScrolled = 0;

	public GuiControlsScrollable(GuiScreen guiscreen, GameSettings gamesettings)
	{
		parentScreen = guiscreen;
		options = gamesettings;
		
		left = 0;
        top = 32;
		slotHeight =  24;
	}


	public void initGui()
	{
		amountScrolled = 0;
		right = width + 80;
		bottom = height - 51;
		
		for(int id = 0; id < options.keyBindings.length; id++) {
			controlList.add(new GuiSmallButton(id, 0, 0, 70, 20, options.getOptionDisplayString(id)));
		}
		StringTranslate stringtranslate = StringTranslate.getInstance();
		controlList.add(new GuiButton(-200, width / 2 - 100, height - 39, stringtranslate.translateKey("gui.done")));
		screenTitle = stringtranslate.translateKey("controls.title");
	}

	protected void actionPerformed(GuiButton guibutton){
		if(guibutton.id == -200){
			mc.displayGuiScreen(parentScreen);
		}
		else {
			for(int i = 0; i < options.keyBindings.length; i++){
				
				((GuiButton)controlList.get(i)).displayString = options.getOptionDisplayString(i);
			}

			selectedButton = guibutton.id;
			guibutton.displayString = (new StringBuilder()).append("> ").append(options.getOptionDisplayString(guibutton.id)).append(" <").toString();
		}
	}

	protected void mouseClicked(int mouseX, int mouseY, int button) {
		if (button == 0) {
			boolean found = false;
			for(int l = 0; l < controlList.size(); l++) {
				GuiButton guibutton = (GuiButton)controlList.get(l);
				if(guibutton.mousePressed(mc, mouseX, mouseY)) {
					mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
					actionPerformed(guibutton);
					found = true;
				}
			}
			
			if (!found){
				if (mouseY > top && mouseY < bottom){
					drag = true;
					dragY = mouseY;
					
					int listRight = width / 2 + 124;
					int scrollRight = listRight + 6;
					if(mouseX >= listRight && mouseX <= scrollRight) {
						scrollMultiplier = -1F;
						int contentHeight = getContentHeight() - (bottom - top - 4);
						if(contentHeight < 1) {
							contentHeight = 1;
						}
						int i4 = (int)((float)((bottom - top) * (bottom - top)) / (float)getContentHeight());
						if(i4 < 32) {
							i4 = 32;
						}
						if(i4 > bottom - top - 8) {
							i4 = bottom - top - 8;
						}
						scrollMultiplier /= (float)(bottom - top - i4) / (float)contentHeight;
					} else {
						scrollMultiplier = 1F;
					}
				}
			}
		}
		super.mouseClicked(mouseX, mouseY, button);
	}

	protected void mouseMovedOrUp(int mouseX, int mouseY, int button) {
		if (drag){
			if (button < 0){
				if (Mouse.isButtonDown(0)){
					updateScrolled(((float)mouseY - dragY) * scrollMultiplier);
					dragY = mouseY;
				}
			} else if (button == 0) {
				drag = false;
			}
		}
		super.mouseMovedOrUp(mouseX, mouseY, button);
	}
	
	public void updateScrolled(float amount){
		int i = getContentHeight() - (bottom - top - 4);
		if(i < 0) {
			i /= 2;
		}

		amountScrolled += amount;
		if(amountScrolled < 0.0F) {
			amountScrolled = 0.0F;
		} else if(amountScrolled > (float)i) {
			amountScrolled = i;
		}
	}

	protected void keyTyped(char key, int keyId) {
		if(selectedButton >= 0) {
            if (keyId == 1) {
                options.setKeyBinding(selectedButton, 0);
            }
            else {
            	options.setKeyBinding(selectedButton, keyId);
            }
            selectedButton = -1;
		}
		else {
			super.keyTyped(key, keyId);
		}
	}

	public void handleMouseInput() {
        int amount = Mouse.getEventDWheel();
        if(amount != 0 && getContentHeight() - (bottom - top - 4) > 0) {
        	if(amount > 0) {
    			amount = -1;
    		} else if(amount < 0) {
    			amount = 1;
    		}
        	updateScrolled((amount * slotHeight) / 2);
        }
		super.handleMouseInput();
	}
	
	protected int getContentHeight() {
		return getSize() * slotHeight;
	}
	
	public int getSize() {
		return (int) (options.keyBindings.length + 2 - 1) / 2;
	}


	public void drawScreen(int mouseX, int mouseY, float f){
		//drawDefaultBackground();
		int size = getSize();
		int l = right / 2 + 124;
		int i1 = l + 6;

		GL11.glDisable(2896 /*GL_LIGHTING*/);
		GL11.glDisable(2912 /*GL_FOG*/);
		Tessellator tessellator = Tessellator.instance;
		GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, mc.renderEngine.getTexture("/gui/background.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		float f1 = 32F;
		tessellator.startDrawingQuads();
		tessellator.setColorOpaque_I(0x202020);
		tessellator.addVertexWithUV(left, bottom, 0.0D, (float)left / f1, (float)(bottom + (int)amountScrolled) / f1);
		tessellator.addVertexWithUV(right, bottom, 0.0D, (float)right / f1, (float)(bottom + (int)amountScrolled) / f1);
		tessellator.addVertexWithUV(right, top, 0.0D, (float)right / f1, (float)(top + (int)amountScrolled) / f1);
		tessellator.addVertexWithUV(left, top, 0.0D, (float)left / f1, (float)(top + (int)amountScrolled) / f1);
		tessellator.draw();
 
		int i2 = right / 2 - 92 - 16;
		int k2 = (top + 4) - (int)amountScrolled;
		for(int i3 = 0; i3 < size; i3++) {
			int k3 = k2 + i3 * slotHeight;
			int j4 = slotHeight - 4;
			if(k3 > bottom || k3 + j4 < top) {
				continue;
			}
			
			int left2 = width / 2 - 155;
			int start = (i3 * 2);
			for (int i = start; (i < start + 2) && (i < options.keyBindings.length); i++) {
				int offset = (i % 2) * 160;
				if (i < controlList.size()) {
					drawString(fontRenderer, options.getKeyBindingDescription(i), left2 + offset + 70 + 6, k3 + 7, -1);
					boolean duplicate = false;
					for(int j = 0; j < options.keyBindings.length & !duplicate; j++) {
						if(i != j && options.keyBindings[i].keyCode == options.keyBindings[j].keyCode){
							duplicate = true;
						}
					}
					GuiButton button = (GuiButton) controlList.get(i);
					if(selectedButton == i) {
						button.displayString = "\247f> \247e??? \247f<";
					} else if (duplicate && options.keyBindings[i].keyCode != 0) {
						 button.displayString = (new StringBuilder()).append("\247c").append(options.getOptionDisplayString(i)).toString();
					} else {
						button.displayString = options.getOptionDisplayString(i);
					}
					button.xPosition = left2 + offset;
					button.yPosition = k3;
					button.drawButton(mc, mouseX, mouseY);
				}
			}
		}

		GL11.glDisable(2929 /*GL_DEPTH_TEST*/);
		byte byte0 = 4;
		overlayBackground(0, top, 255, 255);
		overlayBackground(bottom, height, 255, 255);
		GL11.glEnable(3042 /*GL_BLEND*/);
		GL11.glBlendFunc(770, 771);
		GL11.glDisable(3008 /*GL_ALPHA_TEST*/);
		GL11.glShadeModel(7425 /*GL_SMOOTH*/);
		GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
		tessellator.startDrawingQuads();
		tessellator.setColorRGBA_I(0, 0);
		tessellator.addVertexWithUV(left, top + byte0, 0.0D, 0.0D, 1.0D);
		tessellator.addVertexWithUV(right, top + byte0, 0.0D, 1.0D, 1.0D);
		tessellator.setColorRGBA_I(0, 255);
		tessellator.addVertexWithUV(right, top, 0.0D, 1.0D, 0.0D);
		tessellator.addVertexWithUV(left, top, 0.0D, 0.0D, 0.0D);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setColorRGBA_I(0, 255);
		tessellator.addVertexWithUV(left, bottom, 0.0D, 0.0D, 1.0D);
		tessellator.addVertexWithUV(right, bottom, 0.0D, 1.0D, 1.0D);
		tessellator.setColorRGBA_I(0, 0);
		tessellator.addVertexWithUV(right, bottom - byte0, 0.0D, 1.0D, 0.0D);
		tessellator.addVertexWithUV(left, bottom - byte0, 0.0D, 0.0D, 0.0D);
		tessellator.draw();
		int contentHeight =getContentHeight() - (bottom - top - 4);
		if(contentHeight > 0) {
			int k4 = ((bottom - top) * (bottom - top)) / getContentHeight();
			if(k4 < 32) {
				k4 = 32;
			}
			if(k4 > bottom - top - 8) {
				k4 = bottom - top - 8;
			}
			int i5 = ((int)amountScrolled * (bottom - top - k4)) / contentHeight + top;
			if(i5 < top) {
				i5 = top;
			}
			tessellator.startDrawingQuads();
			tessellator.setColorRGBA_I(0, 255);
			tessellator.addVertexWithUV(l, bottom, 0.0D, 0.0D, 1.0D);
			tessellator.addVertexWithUV(i1, bottom, 0.0D, 1.0D, 1.0D);
			tessellator.addVertexWithUV(i1, top, 0.0D, 1.0D, 0.0D);
			tessellator.addVertexWithUV(l, top, 0.0D, 0.0D, 0.0D);
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setColorRGBA_I(0x808080, 255);
			tessellator.addVertexWithUV(l, i5 + k4, 0.0D, 0.0D, 1.0D);
			tessellator.addVertexWithUV(i1, i5 + k4, 0.0D, 1.0D, 1.0D);
			tessellator.addVertexWithUV(i1, i5, 0.0D, 1.0D, 0.0D);
			tessellator.addVertexWithUV(l, i5, 0.0D, 0.0D, 0.0D);
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setColorRGBA_I(0xc0c0c0, 255);
			tessellator.addVertexWithUV(l, (i5 + k4) - 1, 0.0D, 0.0D, 1.0D);
			tessellator.addVertexWithUV(i1 - 1, (i5 + k4) - 1, 0.0D, 1.0D, 1.0D);
			tessellator.addVertexWithUV(i1 - 1, i5, 0.0D, 1.0D, 0.0D);
			tessellator.addVertexWithUV(l, i5, 0.0D, 0.0D, 0.0D);
			tessellator.draw();
		}
		GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
		GL11.glShadeModel(7424 /*GL_FLAT*/);
		GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
		GL11.glDisable(3042 /*GL_BLEND*/);
		drawCenteredString(fontRenderer, screenTitle, width / 2, 20, 0xffffff);

		((GuiButton)controlList.get(controlList.size()-1)).drawButton(mc, mouseX, mouseY);

	}
	
	void overlayBackground(int top, int bottom, int k, int l) {
		Tessellator tessellator = Tessellator.instance;
		GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, mc.renderEngine.getTexture("/gui/background.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		float f = 32F;
		tessellator.startDrawingQuads();
		tessellator.setColorRGBA_I(0x404040, l);
		tessellator.addVertexWithUV(0.0D, bottom, 0.0D, 0.0D, (float)bottom / f);
		tessellator.addVertexWithUV(right, bottom, 0.0D, (float)right / f, (float)bottom / f);
		tessellator.setColorRGBA_I(0x404040, k);
		tessellator.addVertexWithUV(right, top, 0.0D, (float)right / f, (float)top / f);
		tessellator.addVertexWithUV(0.0D, top, 0.0D, 0.0D, (float)top / f);
		tessellator.draw();
	}
	
	
}
