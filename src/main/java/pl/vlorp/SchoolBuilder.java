package pl.vlorp;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.StairsBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class SchoolBuilder {
    static final int W = 33, D = 19;
    static ServerWorld w;
    static int X0, Y0, Z0;

    static final String[][][] NAMES = {
        {
            {"Гардероб", "Кабинет польского", "Кабинет химии", "Столовая"},
            {"Учительская", "Кабинет информатики", "Библиотека", "Кабинет директора"}
        },
        {
            {"Кабинет 21: матан", "Кабинет истории", "Кабинет английского", "Кабинет биологии"},
            {"Спортзал (в шкафу)", "Туалет (не заходи)", "Кабинет ОБЖ", "Актовый зал"}
        }
    };
    // 0 класс, 1 пусто, 2 столовая, 3 библиотека, 4 директор
    static final int[][][] TYPES = {
        { {1, 0, 0, 2}, {1, 0, 3, 4} },
        { {0, 0, 0, 0}, {1, 1, 0, 1} }
    };

    static void sr(int rx, int ry, int rz, BlockState s) {
        w.setBlockState(new BlockPos(X0 + rx, Y0 + ry, Z0 + rz), s, 2);
    }

    static void sr(int rx, int ry, int rz, Block b) {
        sr(rx, ry, rz, b.getDefaultState());
    }

    static void fr(int x1, int y1, int z1, int x2, int y2, int z2, Block b) {
        BlockState s = b.getDefaultState();
        for (int x = Math.min(x1, x2); x <= Math.max(x1, x2); x++)
            for (int y = Math.min(y1, y2); y <= Math.max(y1, y2); y++)
                for (int z = Math.min(z1, z2); z <= Math.max(z1, z2); z++)
                    sr(x, y, z, s);
    }

    static void label(String text, double x, double y, double z) {
        ArmorStandEntity a = new ArmorStandEntity(w, x, y, z);
        a.setInvisible(true);
        a.setNoGravity(true);
        a.setInvulnerable(true);
        a.setCustomName(Text.literal(text));
        a.setCustomNameVisible(true);
        w.spawnEntity(a);
    }

    static void npc(String key, String name, EntityType<? extends MobEntity> t, boolean baby,
                    double rx, double ry, double rz) {
        Npcs.spawn(w, key, name, t, baby, X0 + rx, Y0 + ry, Z0 + rz);
    }

    public static void build(ServerWorld world, BlockPos p) {
        w = world;
        X0 = p.getX() - 16;
        Z0 = p.getZ() + 6;
        Y0 = p.getY() - 1;

        // участок: очистка, газон, дорожка, фундамент
        fr(-4, 1, -10, W + 3, 14, D + 3, Blocks.AIR);
        fr(-4, 0, -10, W + 3, 0, D + 3, Blocks.GRASS_BLOCK);
        fr(14, 0, -10, 18, 0, -1, Blocks.STONE_BRICKS);
        fr(0, 0, 0, W - 1, 0, D - 1, Blocks.SMOOTH_STONE);

        // два этажа
        for (int f = 0; f < 2; f++) {
            int b = 5 * f;
            fr(0, b + 1, 0, W - 1, b + 4, D - 1, Blocks.BRICKS);
            fr(1, b + 1, 1, W - 2, b + 4, D - 2, Blocks.AIR);
            fr(0, b + 5, 0, W - 1, b + 5, D - 1, f == 0 ? Blocks.SMOOTH_STONE : Blocks.STONE_BRICKS);
            for (int x = 2; x <= W - 3; x++)
                if (x % 4 == 2 || x % 4 == 3)
                    for (int y = b + 2; y <= b + 3; y++) {
                        sr(x, y, 0, Blocks.GLASS);
                        sr(x, y, D - 1, Blocks.GLASS);
                    }
            for (int z = 2; z <= D - 3; z++)
                if (z % 4 == 2 || z % 4 == 3)
                    for (int y = b + 2; y <= b + 3; y++) {
                        sr(0, y, z, Blocks.GLASS);
                        sr(W - 1, y, z, Blocks.GLASS);
                    }
            // пол коридора
            fr(1, b, 8, W - 2, b, 10, Blocks.POLISHED_ANDESITE);
            // стены коридора и перегородки между кабинетами
            fr(1, b + 1, 7, W - 2, b + 4, 7, Blocks.WHITE_CONCRETE);
            fr(1, b + 1, 11, W - 2, b + 4, 11, Blocks.WHITE_CONCRETE);
            for (int xe : new int[]{8, 16, 24}) {
                fr(xe, b + 1, 1, xe, b + 4, 6, Blocks.WHITE_CONCRETE);
                fr(xe, b + 1, 12, xe, b + 4, 17, Blocks.WHITE_CONCRETE);
            }
            // двери в кабинеты и свет в коридоре
            for (int i = 0; i < 4; i++) {
                int xs = 1 + 8 * i;
                fr(xs + 3, b + 1, 7, xs + 3, b + 3, 7, Blocks.AIR);
                fr(xs + 3, b + 1, 11, xs + 3, b + 3, 11, Blocks.AIR);
            }
            for (int x : new int[]{10, 14, 18, 22, 26, 30}) sr(x, b + 5, 9, Blocks.SEA_LANTERN);
        }

        // вход и парапет
        fr(15, 1, 0, 17, 3, 0, Blocks.AIR);
        fr(0, 11, 0, W - 1, 11, 0, Blocks.STONE_BRICK_WALL);
        fr(0, 11, D - 1, W - 1, 11, D - 1, Blocks.STONE_BRICK_WALL);
        fr(0, 11, 0, 0, 11, D - 1, Blocks.STONE_BRICK_WALL);
        fr(W - 1, 11, 0, W - 1, 11, D - 1, Blocks.STONE_BRICK_WALL);

        // флагшток: белое и красное
        fr(-2, 1, -3, -2, 8, -3, Blocks.OAK_FENCE);
        sr(-1, 8, -3, Blocks.WHITE_WOOL);
        sr(-1, 7, -3, Blocks.RED_WOOL);
        sr(0, 8, -3, Blocks.WHITE_WOOL);
        sr(0, 7, -3, Blocks.RED_WOOL);

        // лестница
        fr(4, 5, 8, 6, 5, 10, Blocks.AIR);
        BlockState st = Blocks.OAK_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.EAST);
        for (int i = 0; i < 5; i++)
            for (int z = 8; z <= 10; z++) sr(2 + i, 1 + i, z, st);

        // кабинеты
        for (int f = 0; f < 2; f++) {
            int b = 5 * f;
            for (int side = 0; side < 2; side++) {
                boolean north = side == 0;
                int back = north ? 1 : 17;
                int dir = north ? 1 : -1;
                for (int i = 0; i < 4; i++) {
                    int xs = 1 + 8 * i;
                    int type = TYPES[f][side][i];
                    int lz = north ? 3 : 15;
                    sr(xs + 1, b + 5, lz, Blocks.SEA_LANTERN);
                    sr(xs + 5, b + 5, lz, Blocks.SEA_LANTERN);
                    switch (type) {
                        case 0 -> classroom(b, xs, back, dir);
                        case 2 -> canteen(b, xs, back, dir);
                        case 3 -> library(b, xs, back, dir);
                        case 4 -> director(b, xs, back, dir);
                        default -> { }
                    }
                    label("§e" + NAMES[f][side][i], X0 + xs + 3.5, Y0 + b + 3.0, Z0 + (north ? 4.5 : 14.5));
                }
            }
        }

        label("§6§lV LO BIAŁYSTOK", X0 + 16.5, Y0 + 8.0, Z0 - 0.5);
        label("§7Добро пожаловать. Выхода нет.", X0 + 16.5, Y0 + 7.4, Z0 - 0.5);

        // NPC
        npc("guard", "§fОхранник Збышек", EntityType.IRON_GOLEM, false, 16.5, 1, 2.5);
        npc("zoya", "§bТётя Зоя", EntityType.WANDERING_TRADER, false, 20.5, 1, 9.5);
        npc("kacper", "§aКацпер (одноклассник)", EntityType.VILLAGER, true, 12.5, 1, 9.5);
        npc("kasia", "§dКася (отличница)", EntityType.VILLAGER, true, 14.5, 6, 9.5);
        npc("yarek", "§cЯрек (двоечник)", EntityType.VILLAGER, true, 24.5, 6, 9.5);
        teacher("matan", "§6Пани Матан", 1, 0, 0);
        teacher("polski", "§6Пан Польский", 0, 0, 1);
        teacher("chemia", "§6Пани Химия", 0, 0, 2);
        teacher("cook", "§6Пани Галя (повар)", 0, 0, 3);
        teacher("info", "§6Пан Информатик", 0, 1, 1);
        teacher("library", "§6Пани Библиотекарь", 0, 1, 2);
        teacher("pe", "§6Физрук Пан Мариуш", 1, 1, 0);

        // директор
        int xs = 25, back = 17, dir = -1;
        VLoRp.spawnDirector(w, new Vec3d(X0 + xs + 3.5, Y0 + 1, Z0 + back + 4 * dir + 0.5));
    }

    static void teacher(String key, String name, int f, int side, int i) {
        int xs = 1 + 8 * i;
        int back = side == 0 ? 1 : 17;
        int dir = side == 0 ? 1 : -1;
        npc(key, name, EntityType.VILLAGER, false, xs + 3.5, 5 * f + 1, back + 2 * dir + 0.5);
    }

    static void classroom(int b, int xs, int back, int dir) {
        fr(xs + 1, b + 2, back, xs + 5, b + 3, back, Blocks.BLACK_CONCRETE);
        sr(xs + 3, b + 1, back + dir, Blocks.LECTERN);
        for (int row : new int[]{3, 5})
            for (int x : new int[]{xs + 1, xs + 2, xs + 4, xs + 5}) {
                sr(x, b + 1, back + row * dir, Blocks.OAK_FENCE);
                sr(x, b + 2, back + row * dir, Blocks.OAK_PRESSURE_PLATE);
            }
    }

    static void canteen(int b, int xs, int back, int dir) {
        fr(xs + 1, b + 1, back, xs + 5, b + 1, back, Blocks.SMOOTH_QUARTZ);
        sr(xs + 3, b + 2, back, Blocks.CAULDRON);
        for (int row : new int[]{3, 5})
            fr(xs + 1, b + 1, back + row * dir, xs + 5, b + 1, back + row * dir, Blocks.SPRUCE_SLAB);
    }

    static void library(int b, int xs, int back, int dir) {
        fr(xs + 1, b + 1, back, xs + 5, b + 3, back, Blocks.BOOKSHELF);
        sr(xs + 2, b + 1, back + 4 * dir, Blocks.LECTERN);
        sr(xs + 4, b + 1, back + 4 * dir, Blocks.LECTERN);
    }

    static void director(int b, int xs, int back, int dir) {
        int za = Math.min(back, back + 5 * dir), zb = Math.max(back, back + 5 * dir);
        fr(xs, b + 1, za, xs + 6, b + 1, zb, Blocks.RED_CARPET);
        fr(xs, b + 1, back, xs + 6, b + 2, back, Blocks.BOOKSHELF);
        fr(xs + 2, b + 1, back + 2 * dir, xs + 4, b + 1, back + 2 * dir, Blocks.DARK_OAK_PLANKS);
        sr(xs + 3, b + 2, back + 2 * dir, Blocks.SKELETON_SKULL);
        sr(xs, b + 4, back, Blocks.COBWEB);
        sr(xs + 6, b + 4, back, Blocks.COBWEB);
    }
}
