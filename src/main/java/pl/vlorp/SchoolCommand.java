package pl.vlorp;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

public class SchoolCommand {
    public static void init() {
        CommandRegistrationCallback.EVENT.register((d, reg, env) ->
            d.register(CommandManager.literal("shkola").executes(ctx -> {
                ServerPlayerEntity p = ctx.getSource().getPlayerOrThrow();
                ServerWorld w = p.getServerWorld();
                MinecraftServer s = w.getServer();
                p.sendMessage(Text.literal("§eСтроим V LO Białystok..."), false);
                SchoolBuilder.build(w, p.getBlockPos());
                var cm = s.getCommandManager();
                cm.executeWithPrefix(s.getCommandSource(), "gamerule doMobSpawning false");
                cm.executeWithPrefix(s.getCommandSource(), "time set day");
                w.setSpawnPos(p.getBlockPos(), 0f);
                s.getPlayerManager().broadcast(Text.literal(
                    "§a§lШкола готова. §fВход прямо перед вами. Директор уже в кабинете."), false);
                return 1;
            })));
    }
}
