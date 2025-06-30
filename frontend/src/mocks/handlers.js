/*
import { rest } from "msw";

const userInfo = {
    name: "모험가",
    level: 5,
    exp: 1280,
    achievement_count: 7,
    ending_count: 4,
    email: "ayen_user@gmail.com",
    created_at: "2025-06-01"
};

const scenarios = [
    {
        id: 1,
        title: "기억의 숲",
        description: "잃어버린 숲 속의 이야기...",
        image_url: "https://cdn.ayen.app/images/forest.jpg"
    },
    {
        id: 2,
        title: "도심 속의 늑대",
        description: "도시의 그림자 속 진실을 추적합니다.",
        image_url: "https://cdn.ayen.app/images/citywolf.jpg"
    },
    {
        id: 3,
        title: "마지막 승객",
        description: "종착역을 향해 달리는 기차, 단 한 명만이 살아남습니다.",
        image_url: "https://cdn.ayen.app/images/train.jpg"
    },
    {
        id: 4,
        title: "붉은 달의 전설",
        description: "붉은 달이 뜨는 밤, 저주받은 성으로 향합니다.",
        image_url: "https://cdn.ayen.app/images/redmoon.jpg"
    },
    {
        id: 5,
        title: "잊혀진 실험실",
        description: "문이 잠긴 연구소, 비밀이 잠들어 있습니다.",
        image_url: "https://cdn.ayen.app/images/lab.jpg"
    }
];

const sceneMap = {
    "1": {
        content: "당신의 직업을 선택하세요.",
        image_url: "https://cdn.ayen.app/images/class.jpg",
        is_ending: false,
        choices: [
            {
                description: "전사",
                next_scene_id: 2,
                professionStats: { attack: 12, defense: 10, health: 120, mana: 10, intelligence: 5, agility: 5 },
                professionItems: [
                    { name: "강철 검", description: "공격력 +10", effect: 1 },
                    { name: "회복 포션", description: "체력을 30 회복합니다.", effect: 2 }
                ]
            },
            {
                description: "마법사",
                next_scene_id: 2,
                professionStats: { attack: 5, defense: 5, health: 80, mana: 50, intelligence: 15, agility: 5 },
                professionItems: [
                    { name: "불꽃봉", description: "마법 공격력 +15", effect: 3 },
                    { name: "마력 포션", description: "마나 +30", effect: 4 }
                ]
            },
            {
                description: "도적",
                next_scene_id: 2,
                professionStats: { attack: 8, defense: 6, health: 90, mana: 20, intelligence: 10, agility: 15 },
                professionItems: [
                    { name: "단검", description: "민첩함 +10", effect: 5 },
                    { name: "연막탄", description: "적의 시야를 가립니다.", effect: 6 }
                ]
            }
        ]
    },
    "2": {
        content: "모험이 시작되었습니다. 앞에 숲이 보입니다.",
        image_url: "https://cdn.ayen.app/images/scene1.jpg",
        is_ending: false,
        choices: [
            {
                description: "숲으로 들어간다.",
                next_scene_id: 3,
                effect: { agility: 1 }
            },
            {
                description: "다른 길을 찾는다.",
                next_scene_id: 3,
                effect: { intelligence: 1 }
            }
        ]
    },
    "3": {
        content: "작은 괴물을 만났습니다!",
        image_url: "https://cdn.ayen.app/images/scene2.jpg",
        is_ending: false,
        choices: [
            {
                description: "싸운다",
                next_scene_id: 4,
                effect: { health: -10, attack: 2 }
            },
            {
                description: "도망친다",
                next_scene_id: 4,
                effect: { agility: 1 }
            }
        ]
    },
    "4": {
        content: "모닥불을 발견하고 잠시 휴식을 취합니다.",
        image_url: "https://cdn.ayen.app/images/scene3.jpg",
        is_ending: false,
        choices: [
            {
                description: "잠시 쉰다",
                next_scene_id: 5,
                effect: { health: 10 }
            },
            {
                description: "주변을 살핀다",
                next_scene_id: 5,
                effect: { intelligence: 1 }
            }
        ]
    },
    "5": {
        content: "산 속 동굴 입구에 도착했습니다.",
        image_url: "https://cdn.ayen.app/images/scene4.jpg",
        is_ending: false,
        choices: [
            {
                description: "동굴로 들어간다",
                next_scene_id: 6,
                effect: { intelligence: 1 }
            },
            {
                description: "주위를 탐색한다",
                next_scene_id: 6,
                effect: { agility: 1 }
            }
        ]
    },
    "6": {
        content: "동굴 안에서 빛나는 보석을 발견했습니다!",
        image_url: "https://cdn.ayen.app/images/scene5.jpg",
        is_ending: false,
        choices: [
            {
                description: "보석을 가져간다",
                next_scene_id: 7,
                effect: {
                    mana: 10,
                    addItem: { name: "빛나는 보석", description: "마력을 품은 보석입니다." }
                }
            },
            {
                description: "그냥 둔다",
                next_scene_id: 7,
                effect: {}
            }
        ]
    },
    "7": {
        content: "거대한 문이 앞을 막고 있습니다. 안에는 무언가 있습니다...",
        image_url: "https://cdn.ayen.app/images/scene6.jpg",
        is_ending: false,
        choices: [
            {
                description: "빛나는 보석으로 문을 연다",
                next_scene_id: 8,
                required_item: "빛나는 보석",
                effect: { attack: 1 }
            },
            {
                description: "열쇠를 찾아본다",
                next_scene_id: 8,
                effect: { intelligence: 2 }
            }
        ]
    },
    "8": {
        content: "드디어 보스를 만났습니다!",
        image_url: "https://cdn.ayen.app/images/scene7.jpg",
        is_ending: true,
        choices: [
            {
                description: "보스를 물리치고 다시 시작한다",
                next_scene_id: 1,
                effect: { health: -20, attack: 5 }
            }
        ],
        items: [
            { name: "보스의 검", description: "강력한 힘이 깃든 검입니다." }
        ],
        stats: { attack: 3, health: 10 }
    }
};

const rankings = [
    { name: "용감한 토끼", level: 12, achievement_count: 18 },
    { name: "지혜로운 여우", level: 11, achievement_count: 16 },
    { name: "빛의 검사", level: 11, achievement_count: 14 },
    { name: "모험가", level: 10, achievement_count: 10 },
    { name: "어둠의 기사", level: 9, achievement_count: 8 }
];

const achievements = [
    { id: 1, title: "노 맨즈 랜드", achievedAt: "2025-06-18 14:20" },
    { id: 2, title: "블랙 아웃", achievedAt: "2025-06-17 21:05" },
    { id: 3, title: "신인 작가 단편선", achievedAt: "2025-06-16 19:30" },
    { id: 4, title: "미. 연. 시", achievedAt: "2025-06-15 10:15" },
    { id: 5, title: "잠실 갈리파", achievedAt: "2025-06-14 09:42" },
    { id: 6, title: "분노의 도로", achievedAt: "2025-06-12 22:10" },
];

const endings = [
    { id: 1, title: "기억의 숲 - 해피엔딩", achievedAt: "2025-06-10 18:30" },
    { id: 2, title: "기억의 숲 - 배드엔딩", achievedAt: "2025-06-09 15:45" },
    { id: 3, title: "도심 속 늑대 - 생존", achievedAt: "2025-06-08 20:10" },
    { id: 4, title: "도심 속 늑대 - 희생", achievedAt: "2025-06-07 13:55" },
];

export const handlers = [
    // 시나리오 목록
    rest.get("/api/scenarios", (req, res, ctx) => {
        if (scenarios.length === 0) {
            return res(ctx.status(204), ctx.json({ code: 204, data: [], msg: "데이터가 존재하지 않습니다." }));
        }
        return res(ctx.status(200), ctx.json({ code: 200, data: scenarios, msg: "200 ok" }));
    }),

    // 장면 데이터
    rest.get("/api/scenarios/:scenarioId/scenes/:sceneId", (req, res, ctx) => {
        const { sceneId } = req.params;
        const data = sceneMap[sceneId];
        return data
            ? res(ctx.status(200), ctx.json({ code: 200, data }))
            : res(ctx.status(404), ctx.json({ code: 404, msg: "장면 없음" }));
    }),

    // 선택 처리
    rest.post("/api/choose", async (req, res, ctx) => {
        const body = await req.json();
        console.log("선택 데이터:", body);
        return res(ctx.status(201), ctx.json({ code: 201, msg: "선택 완료" }));
    }),

    // 랭킹 데이터
    rest.get("/api/rankings", (req, res, ctx) => {
        if (rankings.length === 0) {
            return res(ctx.status(204), ctx.json({ code: 204, data: [], msg: "데이터가 존재하지 않습니다." }));
        }
        return res(ctx.status(200), ctx.json({ code: 200, data: rankings, msg: "200 ok" }));
    }),

    // 업적 목록
    rest.get("/api/achievements", (req, res, ctx) => {
        if (achievements.length === 0) {
            return res(ctx.status(204), ctx.json({ code: 204, data: [], msg: "데이터가 존재하지 않습니다." }));
        }
        const list = achievements.map(({ id, title, image_url, achieved_at }) => ({
            id,
            title,
            image_url,
            achieved_at
        }));
        return res(ctx.status(200), ctx.json({ code: 200, data: list, msg: "200 ok" }));
    }),

    // 업적 상세
    rest.get("/api/achievements/:achievementId", (req, res, ctx) => {
        const { achievementId } = req.params;
        const data = achievements.find(a => a.id === Number(achievementId));
        if (!data) {
            return res(ctx.status(204), ctx.json({ code: 204, data: [], msg: "데이터가 존재하지 않습니다." }));
        }
        return res(ctx.status(200), ctx.json({ code: 200, data, msg: "200 ok" }));
    }),

    // 결말 목록
    rest.get("/api/endings", (req, res, ctx) => {
        if (endings.length === 0) {
            return res(ctx.status(204), ctx.json({ code: 204, data: [], msg: "데이터가 존재하지 않습니다." }));
        }
        const list = endings.map(({ id, title, image_url, achieved_at }) => ({
            id,
            title,
            image_url,
            achieved_at
        }));
        return res(ctx.status(200), ctx.json({ code: 200, data: list, msg: "200 ok" }));
    }),

    // 결말 상세
    rest.get("/api/endings/:endingId", (req, res, ctx) => {
        const { endingId } = req.params;
        const data = endings.find(e => e.id === Number(endingId));
        if (!data) {
            return res(ctx.status(204), ctx.json({ code: 204, data: [], msg: "데이터가 존재하지 않습니다." }));
        }
        return res(ctx.status(200), ctx.json({ code: 200, data, msg: "200 ok" }));
    }),

    // 사용자 정보
    rest.get("/api/users/my", (req, res, ctx) => {
        if (!userInfo) {
            return res(ctx.status(404), ctx.json({ code: 404, msg: "해당 사용자를 찾을 수 없습니다." }));
        }
        return res(ctx.status(200), ctx.json({ code: 200, data: userInfo, msg: "200 ok" }));
    })
];
*/