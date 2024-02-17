INSERT INTO MEMBER (sub, description, email, nickname, image_url, created_at, updated_at)
VALUES (1, '회원이다1', 'abc1@email.com', '회원1', 'http://example.com/image1.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO MEMBER (sub, description, email, nickname, image_url, created_at, updated_at)
VALUES (2, '회원이다2', 'abc2@email.com', '회원2', 'http://example.com/image2.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO MEMBER (sub, description, email, nickname, image_url, created_at, updated_at)
VALUES (3, '회원이다3', 'abc3@email.com', '회원3', 'http://example.com/image3.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO MEMBER (sub, description, email, nickname, image_url, created_at, updated_at)
VALUES (4, '회원이다4', 'abc4@email.com', '회원4', 'http://example.com/image4.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO MEMBER (sub, description, email, nickname, image_url, created_at, updated_at)
VALUES (5, '회원이다5', 'abc5@email.com', '회원5', 'http://example.com/image5.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);


INSERT INTO THEME (theme_concept, created_at, updated_at)
VALUES ('건강운', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO THEME (theme_concept, created_at, updated_at)
VALUES ('행운', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO THEME (theme_concept, created_at, updated_at)
VALUES ('재물운', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO THEME (theme_concept, created_at, updated_at)
VALUES ('애정운', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO CARD (member_id, theme_id, image_url, back_name, back_mood, back_content, comment_count, created_at, updated_at)
VALUES (1, 1, 'http://example.com/image.jpg', '따봉도치1', '행복해1', '뭐든 더 써봐1', 31, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO CARD (member_id, theme_id, image_url, back_name, back_mood, back_content, comment_count, created_at, updated_at)
VALUES (2, 2, 'http://example.com/image.jpg', '따봉도치2', '행복해2', '뭐든 더 써봐2', 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO CARD (member_id, theme_id, image_url, back_name, back_mood, back_content, comment_count, created_at, updated_at)
VALUES (1, 3, 'http://example.com/image.jpg', '따봉도치3', '행복해3', '뭐든 더 써봐3', 29, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO CARD (member_id, theme_id, image_url, back_name, back_mood, back_content, comment_count, created_at, updated_at)
VALUES (2, 4, 'http://example.com/image.jpg', '따봉도치4', '행복해4', '뭐든 더 써봐4', 28, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO CARD (member_id, theme_id, image_url, back_name, back_mood, back_content, comment_count, created_at, updated_at)
VALUES (1, 1, 'http://example.com/image.jpg', '따봉도치5', '행복해5', '뭐든 더 써봐5', 27, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO CARD (member_id, theme_id, image_url, back_name, back_mood, back_content, comment_count, created_at, updated_at)
VALUES (2, 2, 'http://example.com/image.jpg', '따봉도치6', '행복해6', '뭐든 더 써봐6', 26, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO CARD (member_id, theme_id, image_url, back_name, back_mood, back_content, comment_count, created_at, updated_at)
VALUES (1, 3, 'http://example.com/image.jpg', '따봉도치7', '행복해7', '뭐든 더 써봐7', 25, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO CARD (member_id, theme_id, image_url, back_name, back_mood, back_content, comment_count, created_at, updated_at)
VALUES (2, 4, 'http://example.com/image.jpg', '따봉도치8', '행복해8', '뭐든 더 써봐8', 24, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO CARD (member_id, theme_id, image_url, back_name, back_mood, back_content, comment_count, created_at, updated_at)
VALUES (1, 1, 'http://example.com/image.jpg', '따봉도치9', '행복해9', '뭐든 더 써봐9', 23, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO CARD (member_id, theme_id, image_url, back_name, back_mood, back_content, comment_count, created_at, updated_at)
VALUES (2, 2, 'http://example.com/image.jpg', '따봉도치10', '행복해10', '뭐든 더 써봐10', 22, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO CARD (member_id, theme_id, image_url, back_name, back_mood, back_content, comment_count, created_at, updated_at)
VALUES (1, 3, 'http://example.com/image.jpg', '따봉도치11', '행복해11', '뭐든 더 써봐11', 21, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO CARD (member_id, theme_id, image_url, back_name, back_mood, back_content, comment_count, created_at, updated_at)
VALUES (2, 4, 'http://example.com/image.jpg', '따봉도치12', '행복해12', '뭐든 더 써봐12', 20, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO CARD (member_id, theme_id, image_url, back_name, back_mood, back_content, comment_count, created_at, updated_at)
VALUES (1, 1, 'http://example.com/image.jpg', '따봉도치13', '행복해13', '뭐든 더 써봐13', 19, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO CARD (member_id, theme_id, image_url, back_name, back_mood, back_content, comment_count, created_at, updated_at)
VALUES (2, 2, 'http://example.com/image.jpg', '따봉도치14', '행복해14', '뭐든 더 써봐14', 18, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO CARD (member_id, theme_id, image_url, back_name, back_mood, back_content, comment_count, created_at, updated_at)
VALUES (1, 3, 'http://example.com/image.jpg', '따봉도치15', '행복해15', '뭐든 더 써봐15', 17, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO CARD (member_id, theme_id, image_url, back_name, back_mood, back_content, comment_count, created_at, updated_at)
VALUES (2, 4, 'http://example.com/image.jpg', '따봉도치16', '행복해16', '뭐든 더 써봐16', 16, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO CARD (member_id, theme_id, image_url, back_name, back_mood, back_content, comment_count, created_at, updated_at)
VALUES (1, 1, 'http://example.com/image.jpg', '따봉도치17', '행복해17', '뭐든 더 써봐17', 15, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO CARD (member_id, theme_id, image_url, back_name, back_mood, back_content, comment_count, created_at, updated_at)
VALUES (2, 2, 'http://example.com/image.jpg', '따봉도치18', '행복해18', '뭐든 더 써봐18', 14, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO CARD (member_id, theme_id, image_url, back_name, back_mood, back_content, comment_count, created_at, updated_at)
VALUES (1, 3, 'http://example.com/image.jpg', '따봉도치19', '행복해19', '뭐든 더 써봐19', 13, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO CARD (member_id, theme_id, image_url, back_name, back_mood, back_content, comment_count, created_at, updated_at)
VALUES (2, 4, 'http://example.com/image.jpg', '따봉도치20', '행복해20', '뭐든 더 써봐20', 12, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO CARD (member_id, theme_id, image_url, back_name, back_mood, back_content, comment_count, created_at, updated_at)
VALUES (1, 1, 'http://example.com/image.jpg', '따봉도치21', '행복해21', '뭐든 더 써봐21', 11, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO CARD (member_id, theme_id, image_url, back_name, back_mood, back_content, comment_count, created_at, updated_at)
VALUES (2, 2, 'http://example.com/image.jpg', '따봉도치22', '행복해22', '뭐든 더 써봐22', 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO CARD (member_id, theme_id, image_url, back_name, back_mood, back_content, comment_count, created_at, updated_at)
VALUES (1, 3, 'http://example.com/image.jpg', '따봉도치23', '행복해23', '뭐든 더 써봐23', 9, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO CARD (member_id, theme_id, image_url, back_name, back_mood, back_content, comment_count, created_at, updated_at)
VALUES (2, 4, 'http://example.com/image.jpg', '따봉도치24', '행복해24', '뭐든 더 써봐24', 8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO CARD (member_id, theme_id, image_url, back_name, back_mood, back_content, comment_count, created_at, updated_at)
VALUES (1, 1, 'http://example.com/image.jpg', '따봉도치25', '행복해25', '뭐든 더 써봐25', 7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO CARD (member_id, theme_id, image_url, back_name, back_mood, back_content, comment_count, created_at, updated_at)
VALUES (2, 2, 'http://example.com/image.jpg', '따봉도치26', '행복해26', '뭐든 더 써봐26', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO CARD (member_id, theme_id, image_url, back_name, back_mood, back_content, comment_count, created_at, updated_at)
VALUES (1, 3, 'http://example.com/image.jpg', '따봉도치27', '행복해27', '뭐든 더 써봐27', 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO CARD (member_id, theme_id, image_url, back_name, back_mood, back_content, comment_count, created_at, updated_at)
VALUES (2, 4, 'http://example.com/image.jpg', '따봉도치28', '행복해28', '뭐든 더 써봐28', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO CARD (member_id, theme_id, image_url, back_name, back_mood, back_content, comment_count, created_at, updated_at)
VALUES (1, 1, 'http://example.com/image.jpg', '따봉도치29', '행복해29', '뭐든 더 써봐29', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);


-- 삭제 테스트 용도 카드
INSERT INTO CARD (member_id, theme_id, image_url, back_name, back_mood, back_content, comment_count, created_at, updated_at)
VALUES (2, 2, 'http://example.com/image.jpg', '따봉도치30', '행복해30', '뭐든 더 써봐30', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);


INSERT INTO COMMENT (member_id, card_id, created_at, updated_at)
VALUES (2, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO COMMENT (member_id, card_id, created_at, updated_at)
VALUES (5, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);


INSERT INTO COMMENT (member_id, card_id, created_at, updated_at)
VALUES (4, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO COMMENT (member_id, card_id, created_at, updated_at)
VALUES (3, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO COMMENT (member_id, card_id, created_at, updated_at)
VALUES (5, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO COMMENT (member_id, card_id, created_at, updated_at)
VALUES (1, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);


INSERT INTO TODAY_CARD (card_id, today_comment_count, created_at, updated_at)
VALUES (10, 22, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO TODAY_CARD (card_id, today_comment_count, created_at, updated_at)
VALUES (2, 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO TODAY_CARD (card_id, today_comment_count, created_at, updated_at)
VALUES (3, 29, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
