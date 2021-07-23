package io.github.PheonixVX.ThreeDShareware.mixin.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.resource.ResourceManager;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

	@Shadow @Final private MinecraftClient client;

	private int textWidth;
	private int field_19254;

	@Inject(at = @At("TAIL"), method = "<init>")
	public void injectGameRenderer (MinecraftClient client, ResourceManager resourceManager, BufferBuilderStorage bufferBuilderStorage, CallbackInfo ci) {
		this.field_19254 = 5;
		this.textWidth = 1;
	}

	@Inject(at = @At("TAIL"), method = "render")
	public void render (float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
		if (!(this.client.getOverlay() instanceof SplashOverlay)) {
			GL11.glDisable(GL11.GL_LIGHTING);
			int var1 = this.client.getWindow().getScaledWidth();
			TextRenderer textRenderer = this.client.textRenderer;
			MatrixStack stack = new MatrixStack();
			InGameHud.fill(stack, 0, 0, var1, 9 + 4, -65536);
			textRenderer.draw(stack, "UNREGISTERED VERSION", (float) this.textWidth, 4.0F, -16711936);

			if (this.field_19254++ > 10) {
				this.field_19254 = 0;
				this.textWidth += 5;
				if (this.textWidth > var1) {
					this.textWidth = -textRenderer.getWidth("UNREGISTERED VERSION");
				}
			}
			GL11.glEnable(GL11.GL_LIGHTING);
		}
	}
}
