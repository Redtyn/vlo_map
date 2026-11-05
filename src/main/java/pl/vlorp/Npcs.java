package pl.vlorp;

import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Npcs {
    private static final Random R = new Random();
    private static final Set<String> TEACHERS = Set.of("matan", "polski", "chemia", "info", "pe", "library");
    public static final Map<String, List<String>> LINES = new HashMap<>();

    static {
        LINES.put("matan", List.of(
            "Дискриминант отрицательный. Как и твои перспективы, как и моя зарплата.",
            "Кто не решит, тот решит пересдачу. Пересдача в воскресенье. В 6:00.",
            "Ты думал, что в жизни матан не пригодится? Мне тоже так говорили. Теперь я здесь.",
            "Вася, ты неправ. Вася, ты вообще был прав когда-нибудь?",
            "Сидим, ждём. Урок закончится, когда я скажу. Или когда ты потеряешь сознание."));
        LINES.put("polski", List.of(
            "Czy ty w ogóle czytałeś lekturę? Нет? Тогда sprawdzian на всё сразу.",
            "Кроме «Пана Тадеуша» в этой жизни тебе ничего не поможет. И «Пан Тадеуш» не поможет.",
            "Выучи наизусть 4 страницы. До утра. Не выучишь до утра, выучишь утром.",
            "Матура близко. Очень. Я уже слышу, как она дышит тебе в затылок."));
        LINES.put("chemia", List.of(
            "Не смешивай всё подряд. Хотя смешай. Директор давно мешает.",
            "В прошлом году тут была химичка Люба. Теперь тут табличка «Люба». Табличка осталась.",
            "Правило первое: не нюхать. Правило второе: если уже понюхал, привет предкам.",
            "Веселящий газ — это смех. Директор смеялся. Теперь он не смеётся, только топором."));
        LINES.put("info", List.of(
            "Librus лёг. Это не баг, это эвакуация журнала.",
            "Выключи и включи. Это не про компьютер, это про жизнь.",
            "Пароль от учительской admin123. Только не говори директору, что ты это знаешь.",
            "Хочешь взломать оценки? Хочу тоже. Но у директора хорошая защита: топор."));
        LINES.put("library", List.of(
            "Тише. Книги спят. Некоторые навсегда.",
            "Сдать «Пана Тадеуша» с 2009 года: штраф 500 злотых. Или ночь здесь.",
            "Не удивлена, что ты ничего не читал. Удивлена, что ты дожил до этого возраста.",
            "Тут была читательница. Читала три года и не заметила, что директор запер дверь снаружи."));
        LINES.put("cook", List.of(
            "Суп дня: суп. Что в нём, лучше не спрашивать. Но ты уже ешь.",
            "Котлета с пюре. Котлета вчерашняя. Пюре позавчерашнее. Ты сегодняшний.",
            "Не хочешь есть, не ешь. Но потом не жалуйся, что директор тебя не накормил. Он любит кормить.",
            "Борщ закончился. Остался компот. Из чего? Не спрашивай."));
        LINES.put("guard", List.of(
            "Пропуск? Нет пропуска? Ну проходи, я не при исполнении. Я вообще не при чём.",
            "Ночью школа не пустая. Это я тебе как человек, который тут ночует. Уже третий год.",
            "Директор с утра нервный. Говорит, ученики плохо учатся. Я бы на твоём месте не попадался.",
            "Я всё видел. Я никому не скажу. Мне не платят за слова."));
        LINES.put("zoya", List.of(
            "Вот намыла, иди по грязи! А ну стоять! Ботинки!",
            "Раньше тут была другая уборщица. Теперь её нет. Никто не спрашивает почему.",
            "Тряпка — оружие. Швабра — оружие. Директор — оружие. Я — свидетель.",
            "Ничего не трогай. Пол мокрый. Всё мокрое. Жить трудно."));
        LINES.put("pe", List.of(
            "Раз-два! Раз-два! Бегом, пока директор не включил режим охоты.",
            "Отжимания — это просто. Особенно когда сзади директор с топором.",
            "Освобождение от физры? Справка от директора? Тогда ты пропал.",
            "Хорошо, но мало. Ещё три круга. И ещё три. Пока не упадёшь."));
        LINES.put("kacper", List.of(
            "Дай списать, а? У меня уже три двойки в Librus, я на грани отчисления.",
            "Ты слышал, что директор вчера сделал? Нет? И не слушай. Лучше не знать.",
            "Ну чё, каво? Всё нормально, живём. Ну... пока.",
            "Пацаны, вообще ребята. Давай прогуляем второй урок. А то директор... ну ты понял."));
        LINES.put("kasia", List.of(
            "Я сделала ДЗ по всем предметам. И на следующую неделю. Пани Матан сказала «молодец». Я до сих пор плачу.",
            "Я готовлюсь к матуре с первого класса. Я не спала с 2022 года.",
            "Не мешай, у меня реферат по директору. Тема: «Почему он не спит»."));
        LINES.put("yarek", List.of(
            "У меня двойка по всему. Это не лень, это протест против системы.",
            "Если директор спросит, где я был, меня тут не было. Меня вообще не существует.",
            "Я хочу в Незер. Там хотя бы нет ДЗ."));
    }

    public static void init() {
        UseEntityCallback.EVENT.register((player, world, hand, entity, hit) -> {
            if (world.isClient || hand != Hand.MAIN_HAND) return ActionResult.PASS;
            for (String tag : entity.getCommandTags()) {
                if (!tag.startsWith("npc:")) continue;
                String key = tag.substring(4);
                List<String> l = LINES.get(key);
                if (l == null) continue;
                String name = entity.getName().getString();
                player.sendMessage(Text.literal("§e" + name + "§f: " + l.get(R.nextInt(l.size()))), false);
                if (TEACHERS.contains(key) && R.nextInt(4) == 0) {
                    player.giveItemStack(new ItemStack(VLoRp.HOMEWORK));
                    player.sendMessage(Text.literal("§6Тебе выдали домашку. Не благодари."), false);
                }
                return ActionResult.SUCCESS;
            }
            return ActionResult.PASS;
        });
    }

    public static MobEntity spawn(ServerWorld w, String key, String name,
                                  EntityType<? extends MobEntity> type, boolean baby,
                                  double x, double y, double z) {
        MobEntity m = type.create(w);
        if (m == null) return null;
        m.refreshPositionAndAngles(x, y, z, 0f, 0f);
        m.setCustomName(Text.literal(name));
        m.setCustomNameVisible(true);
        m.setAiDisabled(true);
        m.setInvulnerable(true);
        m.setPersistent();
        m.addCommandTag("npc:" + key);
        if (baby) m.setBaby(true);
        w.spawnEntity(m);
        return m;
    }
}
