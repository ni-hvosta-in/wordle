ALTER TABLE user_game RENAME TO user_profile;
ALTER TABLE user_profile Drop column a1_index;
ALTER TABLE user_profile Drop column a2_index;
ALTER TABLE user_profile Drop column b1_index;
ALTER TABLE user_profile Drop column b2_index;