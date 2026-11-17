import { Button, Dropdown, type MenuProps } from 'antd';
import type { Level } from '../../model/Level';

type ProfileProps = {
    level: Level;
    game_level: number;
    items: MenuProps['items'];
    coins?: number;
    isPersonal?: boolean;
    isPlaying?: boolean;
    helpFunction?: () => void;
};

export function Profile({ level, items, coins = 0, game_level, isPersonal, isPlaying, helpFunction }: ProfileProps) {


    return (

        <div className="game-profile-card">
            
            
            <div className="game-profile-eng-level">
                <span>Уровень английского:</span>
                <Dropdown menu={{ items }} placement="top" arrow={true}>
                    <Button>{level}</Button>
                </Dropdown>
            </div>

            {isPersonal && (
                <div className="game-profile-level">
                    <span>Текущий уровень:</span>
                    <span>{game_level}</span>
                </div>
            )}

            {isPersonal && (
                <div className="game-profile-coins">
                    <span>Монеты:</span>
                    <span>{coins}</span>
                </div>
            )}

            {isPersonal && isPlaying && (
                <button type="button" className="game-profile-hint-button" onClick={helpFunction}>
                    Подсказка
                </button>
            )}

        </div>
    );
}
