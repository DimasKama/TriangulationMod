package dimaskama.triangulationmod.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import java.awt.geom.Point2D;

public class TriangulationModClient implements ClientModInitializer {
    public static final String MOD_ID = "triangulationmod";
    private static final KeyBinding KEY_NEW_LINE_1 = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.new_line1",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_COMMA,
            MOD_ID
    ));
    private static final KeyBinding KEY_NEW_LINE_2 = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.new_line2",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_PERIOD,
            MOD_ID
    ));
    private static final KeyBinding KEY_MODE = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.change_mode",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            MOD_ID
    ));
    public static boolean mode = false;
    private static Line line1 = null;
    public static Point2D.Double line1FirstPoint = null;
    private static Line line2 = null;
    public static Point2D.Double line2FirstPoint = null;
    private static Point2D.Double point = null;

    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register(new ModHud());
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;
            while (KEY_MODE.wasPressed()) {
                mode = !mode;
                if (mode) sendModMessage(client.player, Text.translatable("mode_changed_2points"), 0xbbbbbb);
                else {
                    line1FirstPoint = null;
                    line2FirstPoint = null;
                    sendModMessage(client.player, Text.translatable("mode_changed_look_dir"), 0xbbbbbb);
                }
            }
            while (KEY_NEW_LINE_1.wasPressed()) {
                double playerX = client.player.getX();
                double playerZ = client.player.getZ();
                if (!mode) {
                    Vec3d vec3d = client.player.getRotationVector();
                    double x2 = playerX + vec3d.getX();
                    double z2 = playerZ + vec3d.getZ();
                    line1 = new Line(playerX, playerZ, x2, z2);
                    sendModMessage(client.player, Text.translatable("defined_line", 1), 0x999999);
                    if (line2 != null) generatePoint(client.player);
                } else {
                    if (line1FirstPoint == null) {
                        line1FirstPoint = new Point2D.Double(playerX, playerZ);
                        sendModMessage(client.player, Text.translatable("please_mark_2nd_point", 1), 0x999999);
                    } else {
                        line1 = new Line(line1FirstPoint.getX(), line1FirstPoint.getY(), playerX, playerZ);
                        line1FirstPoint = null;
                        sendModMessage(client.player, Text.translatable("defined_line", 1), 0x999999);
                        if (line2 != null) generatePoint(client.player);
                    }
                }
            }
            while (KEY_NEW_LINE_2.wasPressed()) {
                double playerX = client.player.getX();
                double playerZ = client.player.getZ();
                if (!mode) {
                    Vec3d vec3d = client.player.getRotationVector();
                    double x2 = playerX + vec3d.getX();
                    double z2 = playerZ + vec3d.getZ();
                    line2 = new Line(playerX, playerZ, x2, z2);
                    sendModMessage(client.player, Text.translatable("defined_line", 2), 0x999999);
                    if (line1 != null) generatePoint(client.player);
                } else {
                    if (line2FirstPoint == null) {
                        line2FirstPoint = new Point2D.Double(playerX, playerZ);
                        sendModMessage(client.player, Text.translatable("please_mark_2nd_point", 2), 0x999999);
                    } else {
                        line2 = new Line(line2FirstPoint.getX(), line2FirstPoint.getY(), playerX, playerZ);
                        line2FirstPoint = null;
                        sendModMessage(client.player, Text.translatable("defined_line", 2), 0x999999);
                        if (line1 != null) generatePoint(client.player);
                    }
                }
            }
        });
    }
    private static void generatePoint(ClientPlayerEntity player) {
        double x = (line1.b - line2.b) / (line2.k - line1.k);
        Point2D.Double point1 = new Point2D.Double(x, line1.k * x + line1.b);
        if (Math.abs(point1.getX()) > 60000000 || Math.abs(point1.getY()) > 60000000 || Double.isNaN(point1.getX()) || Double.isNaN(point1.getY()))
            sendModMessage(player, Text.translatable("point_out_of_world"), 0xff4444);
        else {
            point = point1;
            sendModMessage(player, Text.translatable("point_found",
                    String.format("X=%.3f | Z=%.3f", point.getX(), point.getY()),
                    (int) Point2D.distance(player.getX(), player.getZ(), point.getX(), point.getY())), 0xffff33);
        }
    }
    private static void sendModMessage(ClientPlayerEntity player, MutableText message, int color) {
        player.sendMessage(Text.literal("[Triangulation] ").setStyle(Style.EMPTY.withColor(0x0099ff))
                .append(message.setStyle(Style.EMPTY.withColor(color))));
    }
}
