DELIMITER //
CREATE TRIGGER update_article_username_photo 
AFTER UPDATE ON user 
FOR EACH ROW 
BEGIN
	IF NEW.username <> OLD.username THEN  -- 仅当username字段实际变化时触发
        UPDATE article 
        SET username = NEW.username 
        WHERE user_id = NEW.id;
    END IF;
    IF NEW.photo <> OLD.photo THEN  -- 仅当photo字段实际变化时触发
        UPDATE article 
        SET photo = NEW.photo 
        WHERE user_id = NEW.id;
    END IF;
END //
DELIMITER ;