package dimaskama.triangulationmod.client;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class ModHud implements HudRenderCallback {
    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        int y = MinecraftClient.getInstance().getWindow().getScaledHeight() / 2;
        if (TriangulationModClient.line1FirstPoint != null) {
            drawContext.drawTextWithShadow(textRenderer, Text.translatable("hud.line", 1), 5, y, 0xffffff);
            y += 10;
            drawContext.drawTextWithShadow(textRenderer,
                    Text.translatable("hud.point1", String.format("X=%.3f, Z=%.3f", TriangulationModClient.line1FirstPoint.getX(), TriangulationModClient.line1FirstPoint.getY())),
                    5, y, 0xffffff);
            y += 10;
            drawContext.drawTextWithShadow(textRenderer, Text.translatable("hud.point2"), 5, y, 0xffffff);
            y += 15;
        }
        if (TriangulationModClient.line2FirstPoint != null) {
            drawContext.drawTextWithShadow(textRenderer, Text.translatable("hud.line", 2), 5, y, 0xffffff);
            y += 10;
            drawContext.drawTextWithShadow(textRenderer,
                    Text.translatable("hud.point1", String.format("X=%.3f, Z=%.3f", TriangulationModClient.line2FirstPoint.getX(), TriangulationModClient.line2FirstPoint.getY())),
                    5, y, 0xffffff);
            y += 10;
            drawContext.drawTextWithShadow(textRenderer, Text.translatable("hud.point2"), 5, y, 0xffffff);
        }
    }
}
