package net.eve0415.spigot.WebsocketIntegration;

public enum Advancement {
    MINECRAFT_STORY_ROOT("Minecraft"),
    MINECRAFT_STORY_MINE_STONE("石器時代"),
    MINECRAFT_STORY_UPGRADE_TOOLS("アップグレード"),
    MINECRAFT_STORY_SMELT_IRON("金属を手に入れる"),
    MINECRAFT_STORY_OBTAIN_ARMOR("装備せよ"),
    MINECRAFT_STORY_LAVA_BUCKET("ホットスタッフ"),
    MINECRAFT_STORY_IRON_TOOLS("鉄のツルハシで決まり"),
    MINECRAFT_STORY_DEFLECT_ARROW("今日はやめておきます"),
    MINECRAFT_STORY_FORM_OBSIDIAN("アイス・バケツ・チャレンジ"),
    MINECRAFT_STORY_MINE_DIAMOND("ダイヤモンド！"),
    MINECRAFT_STORY_ENTER_THE_NETHER("さらなる深みへ"),
    MINECRAFT_STORY_SHINY_GEAR("ダイヤモンドで私を覆って"),
    MINECRAFT_STORY_ENCHANT_ITEM("エンチャントの使い手"),
    MINECRAFT_STORY_CURE_ZOMBIE_VILLAGER("ゾンビドクター"),
    MINECRAFT_STORY_FOLLOW_ENDER_EYE("アイ・スパイ"),
    MINECRAFT_STORY_ENTER_THE_END("おしまい？"),
    MINECRAFT_NETHER_ROOT("ネザー"),
    MINECRAFT_NETHER_RETURN_TO_SENDER("火の玉をガストに打ち返す"),
    MINECRAFT_NETHER_FIND_BASTON("兵どもが夢の跡"),
    MINECRAFT_NETHER_OBTAIN_ANCIENT_DEBRIS("深淵に隠されしもの"),
    MINECRAFT_NETHER_FAST_TRAVEL("亜空間バブル"),
    MINECRAFT_NETHER_FIND_FORTRESS("恐ろしい要塞"),
    MINECRAFT_NETHER_OBTAIN_CRYING_OBSIDIAN("玉ねぎを切っているのは誰？"),
    MINECRAFT_NETHER_DISTRACT_PIGLIN("わーいぴかぴか！"),
    MINECRAFT_NETHER_RIDE_STRIDER("足の生えたボート"),
    MINECRAFT_NETHER_UNEASY_ALLIANCE("不安な同盟"),
    MINECRAFT_NETHER_LOOT_BASTION("ブタ戦争"),
    MINECRAFT_NETHER_USE_LOADSTONE("この道をずっとゆけば"),
    MINECRAFT_NETHER_NETHERITE_ARMOR("残骸で私を覆って"),
    MINECRAFT_NETHER_GET_WITHER_SKULL("不気味で怖いスケルトン"),
    MINECRAFT_NETHER_OBTAIN_BLAZE_ROD("炎の中へ"),
    MINECRAFT_NETHER_CHARGE_RESPAWN_ANCHOR("不死身とまではいかない"),
    MINECRAFT_NETHER_EXPLORE_NETHER("ホットな観光地"),
    MINECRAFT_NETHER_SUMMON_WITHER("荒が丘"),
    MINECRAFT_NETHER_BREW_POTION("町のお薬屋さん"),
    MINECRAFT_NETHER_CREATE_BEACON("生活のビーコン"),
    MINECRAFT_NETHER_ALL_POTIONS("猛烈なカクテル"),
    MINECRAFT_NETHER_CREATE_FULL_BEACON("ビーコネーター"),
    MINECRAFT_NETHER_ALL_EFFECTS("どうやってここまで？"),
    MINECRAFT_END_ROOT("ジ・エンド"),
    MINECRAFT_END_KILL_DRAGON("エンドの解放"),
    MINECRAFT_END_DRAGON_EGG("ザ・ネクストジェネレーション"),
    MINECRAFT_END_ENTER_END_GATEWAY("遠方への逃走"),
    MINECRAFT_END_RESPAWN_DRAGON("おしまい…再び…"),
    MINECRAFT_END_DRAGON_BREATH("口臭に気をつけよう"),
    MINECRAFT_END_FIND_END_CITY("ゲームの果ての都市"),
    MINECRAFT_END_ELYTRA("空はどこまでも高く"),
    MINECRAFT_END_LEVITATE("ここからの素晴らしい眺め"),
    MINECRAFT_ADVENTURE_ROOT("冒険"),
    MINECRAFT_ADVENTURE_VOLUNTARY_EXILE("自主的な亡命"),
    MINECRAFT_ADVENTURE_KILL_A_MOB("モンスターハンター"),
    MINECRAFT_ADVENTURE_TRADE("良い取引だ！"),
    MINECRAFT_ADVENTURE_HONEY_BLOCK_SLIDE("べとべとな状況"),
    MINECRAFT_ADVENTURE_OL_BETSY("おてんば"),
    MINECRAFT_ADVENTURE_SLEEP_IN_BED("良い夢見てね"),
    MINECRAFT_ADVENTURE_HERO_OF_THE_VILLAGE("村の英雄"),
    MINECRAFT_ADVENTURE_THROW_TRIDENT("もったいぶった一言"),
    MINECRAFT_ADVENTURE_SHOOT_ARROW("狙いを定めて"),
    MINECRAFT_ADVENTURE_KILL_ALL_MOBS("モンスター狩りの達人"),
    MINECRAFT_ADVENTURE_TOTEM_OF_UNDYING("死を超えて"),
    MINECRAFT_ADVENTURE_SUMMON_IRON_GOLEM("お手伝いさん"),
    MINECRAFT_ADVENTURE_TWO_BIRDS_ONE_ARROW("一石二鳥"),
    MINECRAFT_ADVENTURE_WHOS_THE_PILLAGER_NOW("どっちが略奪者？"),
    MINECRAFT_ADVENTURE_ARBALISTIC("クロスボウの達人"),
    MINECRAFT_ADVENTURE_ADVENTURING_TIME("冒険の時間"),
    MINECRAFT_ADVENTURE_VERY_VERY_FRIGHTENING("とてもとても恐ろしい"),
    MINECRAFT_ADVENTURE_SNIPER_DUEL("スナイパー対決"),
    MINECRAFT_ADVENTURE_BULLSEYE("的中"),
    MINECRAFT_HUSBANDRY_ROOT("農業"),
    MINECRAFT_HUSBANDRY_SAFELY_HARVEST_HONEY("大切なお客様"),
    MINECRAFT_HUSBANDRY_BREED_AN_ANIMAL("コウノトリの贈り物"),
    MINECRAFT_HUSBANDRY_TAME_AN_ANIMAL("永遠の親友となるだろう"),
    MINECRAFT_HUSBANDRY_FISHY_BUSINESS("生臭い仕事"),
    MINECRAFT_HUSBANDRY_SILK_TOUCH_NEST("完全な引越し"),
    MINECRAFT_HUSBANDRY_PLANT_SEED("種だらけの場所"),
    MINECRAFT_HUSBANDRY_BRED_ALL_ANIMALS("二匹ずつ"),
    MINECRAFT_HUSBANDRY_COMPLETE_CATALOGUE("猫大全集"),
    MINECRAFT_HUSBANDRY_TACTICAL_FISHING("戦術的漁業"),
    MINECRAFT_HUSBANDRY_BALANCED_DIET("バランスの取れた食事"),
    MINECRAFT_HUSBANDRY_BREAK_NETHERITE_HOE("真面目な献身");

    private final String value;

    private Advancement(final String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
