USE [master];
GO
IF DB_ID(N'BookStore') IS NOT NULL
BEGIN
    ALTER DATABASE [BookStore] SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE [BookStore];
END
DROP DATABASE IF EXISTS BookStore;
GO

CREATE DATABASE BookStore;
GO

USE BookStore;
GO

CREATE TABLE dbo.[Users](
	ID NVARCHAR(30) PRIMARY KEY NOT NULL,
	[Password] NVARCHAR(MAX),
	Fullname NVARCHAR(50),
	Email NVARCHAR(50) UNIQUE,
	Gender BIT,
	Birthday DATE,
	Phone NVARCHAR(15) UNIQUE,
	[Address] NVARCHAR(100),
/*	[Role] BIT, xóa role nè check lại user haydt21*/
	Active BIT
)
GO


CREATE TABLE dbo.[Roles](
	Id NVARCHAR(10) PRIMARY KEY NOT NULL,
	[Name] NVARCHAR(50)
)
GO


CREATE TABLE dbo.[Authorities](
	Id BIGINT IDENTITY(1,1) PRIMARY KEY NOT NULL,
	[Username] NVARCHAR(30),
	RoleId NVARCHAR(10)
)
GO


CREATE TABLE dbo.Categories(
	ID NVARCHAR(10) PRIMARY KEY NOT NULL,
	[Name] NVARCHAR(50)
)
GO

CREATE TABLE dbo.Products(
	ID NVARCHAR(10) PRIMARY KEY NOT NULL,
	CategoryID NVARCHAR(10),
	[Name] NVARCHAR(50),
	Price DOUBLE PRECISION,
	Quantity INT,
	[Description] NVARCHAR(MAX),
	[Image] NVARCHAR(100),
	[LIKE] INTEGER DEFAULT 0,
	Author NVARCHAR(50)
)
GO

CREATE TABLE dbo.Carts(
	ID BIGINT PRIMARY KEY NOT NULL IDENTITY (1,1),
	[UserID] NVARCHAR(30),
	ProductID NVARCHAR(10),
	Quantity_purchased INT
)
GO

CREATE TABLE dbo.Orders(
	ID NVARCHAR(10) PRIMARY KEY NOT NULL,
	[UserID] NVARCHAR(30),
	Total DOUBLE PRECISION,
	DateCreate DATE,
	[Address] NVARCHAR(255),
	[Status] INT CHECK (Status IN (0, 1, 2, 3))
)
GO

CREATE TABLE dbo.OrderDetail(
	ID NVARCHAR(10) PRIMARY KEY NOT NULL,
	OrderID NVARCHAR(10),
	ProductID NVARCHAR(10),
	Quantity_purchased INT,
	DateCreate DATE
)
GO

CREATE TABLE dbo.Favorites(
	ID BIGINT PRIMARY KEY NOT NULL IDENTITY (1,1),
	[UserID] NVARCHAR(30),
	ProductID NVARCHAR(10),
	DateCreate DATE
)
GO

ALTER TABLE dbo.Authorities 
ADD CONSTRAINT fk_users_authorities 
FOREIGN KEY (Username) 
REFERENCES dbo.[Users](Id)
GO

ALTER TABLE dbo.Authorities 
ADD CONSTRAINT fk_roles_authorities 
FOREIGN KEY (RoleId) 
REFERENCES dbo.[Roles](Id)
GO

ALTER TABLE dbo.Products 
ADD CONSTRAINT fk_categories_products 
FOREIGN KEY (CategoryID) 
REFERENCES dbo.[Categories](ID)
GO

ALTER TABLE dbo.Carts 
ADD CONSTRAINT fk_products_carts 
FOREIGN KEY (ProductID) 
REFERENCES dbo.[Products](ID)
GO

ALTER TABLE dbo.Carts
ADD CONSTRAINT fk_users_carts 
FOREIGN KEY ([UserID]) 
REFERENCES dbo.[Users](ID)
GO

ALTER TABLE dbo.Orders 
ADD CONSTRAINT fk_users_orders 
FOREIGN KEY ([UserID]) 
REFERENCES dbo.[Users](ID)
GO

ALTER TABLE dbo.OrderDetail 
ADD CONSTRAINT fk_orders_order_detail 
FOREIGN KEY ([OrderID]) 
REFERENCES dbo.[Orders](ID)
GO

ALTER TABLE dbo.OrderDetail 
ADD CONSTRAINT fk_products_order_detail 
FOREIGN KEY (ProductID) 
REFERENCES dbo.[Products](ID)
GO

ALTER TABLE dbo.Favorites 
ADD CONSTRAINT fk_products_favorites 
FOREIGN KEY (ProductID) 
REFERENCES dbo.[Products](ID)
GO

ALTER TABLE dbo.Favorites
ADD CONSTRAINT fk_users_favorites 
FOREIGN KEY ([UserID]) 
REFERENCES dbo.[Users](ID)
GO

INSERT INTO dbo.Categories VALUES
('1', N'Light Novel'),
('2', N'Tiểu thuyết'),
('3', N'Sách tự lực'),
('4', N'Sách thiếu thi'),
('5', N'Sách kinh tế'),
('6', N'Sách đời sống')

GO


INSERT INTO dbo.Products VALUES
('SP001','1',N'ARIFURETA', 120000, 100, N'Hajime, nạn nhân bị bắt nạt và các bạn cùng lớp bị triệu hồi đến một thế giới khác. Trái ngược với chúng bạn với các khả năng chiến đấu siêu cường như hack game, Hajime chỉ có năng lực rất đỗi bình thường là khả năng biến đổi. Đến thế giới khác rồi mà Hajime vẫn trở thành người yếu nhất, thế là một đứa bạn cùng lớp ác ý đã khiến cậu rơi xuống đáy vực thẳm?
Bên bờ vực của tuyệt vọng, khi không còn cách nào thoát thân, Hajime tìm ra con đường để trở thành người mạnh nhất với tư cách một biến đổi sư. Lúc đó, cuộc gặp gỡ định mệnh giữa cậu và nàng ma cà rồng tên Yue cũng diễn ra…

“Tôi sẽ bảo vệ Yue, Yue cũng sẽ bảo vệ tôi. Chúng ta sẽ trở thành người mạnh nhất, chinh phạt tất cả và rời khỏi thế giới này.”

Câu chuyện viễn tưởng “mạnh mẽ nhất” của thiếu niên rơi xuống địa ngục và ma cà rồng ở nơi sâu thẳm ấy, bắt đầu!','hinh1.jpg',12, N'Shirakome Ryo'),
/* */
('SP002','1',N'Hành Trình Của Elaina',110000,100,N'Ngày xửa ngày xưa, có một cô phù thủy tên Elaina. 

Cô đang trong chuyến hành trình chu du tự do, không bị ràng buộc bởi bất cứ ai hay bất cứ chuyện gì. 

Lần này, chuyến ngao du sẽ đưa cô đến gặp những con người đầy cá tính…

Một nhóm thợ săn yêu tinh và yêu tinh bóng tối lang thang tìm kiếm bạn đồng hành. 

Người du hành giàu có trên con đường đấu tranh để lấy lại nụ cười cho con gái mình. 

“Phù thủy than củi” cùng em gái trong nhiệm vụ điều tra bí mật. 

Cặp chị em đang tìm kiếm một mái ấm mới. 

Một nữ phù thủy cá tính mạnh mẽ nhắm đến công việc kinh doanh đầy nhiệt huyết.

Và một thầy trừ tà trẻ tuổi dừng chân tại một ngôi làng nọ.

Những cuộc gặp gỡ và sự kiện lần này sẽ được viết lại trong nhật ký của Elaina như nào đây?

"Chà, nếu tôi đã ra tay thì mọi chuyện sẽ ổn thôi.”','hinh2.jpg',87,N'Jougi Shiraishi'),
/* */
('SP003','1',N'Re:Monster Hồi Sinh Thành Quái Vật',100000,100,N'Tôi bị một kẻ bám đuôi đâm chết, khi tỉnh giấc đã thấy mình đang ở một thế giới khác.

Chợt nhìn lại bản thân thì, da xanh, móng tay đen, vừa nhọn lại vừa sắc bén… Có vẻ tôi đã hóa thành một con yêu tinh Goblin. Bữa ăn dặm là những con sâu bướm bắt được trong hang động. Rồi sau khi chào đời được bốn ngày, tôi đã có chuyến đi săn đầu tiên - bắt đầu cuộc chiến sinh tồn của một con quái thú. 

Bằng cách này hay cách khác, với năng lực “Hấp thụ”, càng ăn càng trở nên mạnh mẽ của mình, tôi tiến bộ rất nhanh, rồi chẳng mấy chốc đã trở thành người đứng đầu thống trị Động Goblin. Tôi thu nạp một nhóm thuộc hạ có năng lực (Gobukichi với Gobumi gì gì đấy), thêm vào đó còn thuần hóa được đám phụ nữ loài người bị bắt cóc. Kẻ nào đe dọa đồng bọn của mình, với tư cách thủ lĩnh, tôi nhất định không dung thứ. 

Dù là quái vật, Goblin hay con người, đã xem như kẻ địch là tôi sẽ ăn, ăn, ăn cho bằng sạch!','hinh3.jpg',40,N'Kanekiru Kogitsune '),
/* */
('SP004','1',N'The Dungeon Seeker',90000,100,N'Vào một ngày nọ, cậu thanh niên Takeda Junpei luôn chịu sự bắt nạt của những người bạn trong lớp cùng tên đầu sỏ chuyên bắt nạt cậu – Kido Shouta và cô bạn từ thuở thơ ấu Tatsumiya Noriko được triệu hồi sang một thế giới khác do sự trêu đùa của Thần. Ngỡ rằng được triệu hồi thành dũng sĩ, không hiểu sao thanh trạng thái của Junpei lại hiển thị cấp độ cặn bã, thấp hơn cả  người.

Nhân cơ hội đó, tên Kido gian xảo và cô bạn phản bội Noriko đã đẩy Junpei xuống một mê cung để làm vật hi sinh thay mình. Nơi đó chính là “Mê cung Giao giới” đầy tiếng kêu la của lũ quái vật. Một địa ngục với tỉ lệ sống sót 0% đã khiến biết bao nhà thám hiểm nổi tiếng phải bỏ mạng.Trong mê cung hỗn loạn chất đầy xác chết của những nhà thám hiểm ấy bất ngờ xuất hiện một con quái vật khủng khiếp, chắn ngang giữa đường.

Dưới tình huống lâm vào bước đường cùng, sẵn sàng cho cái chết, Junpei nhen nhóm kế hoạch tuyệt vời cho sự hồi sinh từ cõi chết của mình. Câu chuyện về con người yếu ớt đi tìm lối thoát khỏi địa ngục, sống sót tại mê cung đầy rẫy khó khăn và hành trình trả thù những kẻ hãm hại mình xin được phép bắt đầu…','hinh4.jpg',19,N'Sakamato 666 '),
/* */
('SP005','1',N'Khách Sạn Yêu Quái Ở Izumo',98000,100,N'Tokimachi Misome đã bắt đầu công việc tại khách sạn ma quái ở Izumo, nơi rộng cửa đón chào các khách hàng thần linh và yêu quái. Một ngày nọ, Misome nhận được tin, người bạn thân của cô – Hirano Yui quyết định ghé thăm Izumo cùng với sếp của mình – một họa sĩ truyện tranh nổi tiếng. Misome đã vô cùng vui mừng khi gặp nhau nhưng người bạn đi cùng với bạn thân cô lại xem chừng có vẻ khó tính hơn rất nhiều.

Bối cảnh của Khách Sạn Yêu Quái Ở Izumo xoay quanh trong Tháng có Thần, quy tụ hầu hết những thần linh trong cả nước và Valentine lấp lánh muôn màu muôn vẻ tình yêu, cuộc phiêu lưu này tiếp tục dẫn dắt những người đọc tham gia vào những sự kiện thú vị được diễn ra tại Izumo.

Khách Sạn Yêu Quái Ở Izumo – Tập 2 sẽ đưa bạn đọc từ bất ngờ này đến bất ngờ khác bên cạnh những điều kì lạ đã và đang diễn ra khi một đất nước có những vị thần linh được hiện hữu. Sẽ là một điều thú vị đang chờ đợi bạn tại tập 2 này','hinh5.jpg',12,N'Garasumachi Hari '),
/* */
('SP006','1',N'World Teacher',130000,100,N'World Teacher là một bộ truyện thuộc thể loại chuyển sinh khá phổ biến trong dòng Light-novel trong những năm gần đây.

Câu chuyện bắt đầu bằng việc điệp viên được ca tụng là tài giỏi nhất thế giới hy sinh trong một điệp vụ. Bỏ lại những học trò thân yêu, ông nguyện hy sinh để chống lại âm mưu thôn tính cả thế giới của một tổ chức xấu xa.

Tuy nhiên, thay vì chết đi, người điệp viên ấy được chuyển sinh sang một thế giới mới, nơi tồn tại phép thuật và cả những sinh vật huyền bí, trở thành cậu bé sơ sinh Sirius. Với những ký ức vẫn còn giữ lại từ thế giới cũ, những kinh nghiệm tích lũy trong sáu mươi năm làm điệp viên và cả ước mơ tiếp tục đào tạo những đệ tử tuyệt vời, người điệp viên ấy liền lập tức khổ luyện ngay từ khi mới chỉ là một cậu bé con.

Nhờ kiến thức và sự chăm chỉ rèn luyện không ngừng, cậu đã nhanh chóng tích lũy được nguồn ma pháp dồi dào và những kiến thức vượt bậc, mang đến nguồn sức mạnh phi thường.

Trong thế giới mới, cậu đã có một gia đình, người thân, bạn bè, đệ tử và cả những thử thách để ngày một trưởng thành. Cậu được cô Erina, người sau này cậu trân trọng gọi là “mẹ” nuôi dưỡng, được Dee và Noel chăm sóc, quen được cô gái Elf cùng với một lời hứa hẹn, cứu được cặp chị em Ngân lang tộc Emilia và Reus.','hinh6.jpg',40,N'Koichi Neko'),
/* */
('SP007','2',N'GAMERS!',150000,100,N'Sở thích là chơi game, ngoài điều đó ra thì Keita Amano – một học sinh cấp 3 cô độc đúng vai nhân vật quần chúng – chẳng có lấy một đặc trưng nổi bật, cũng chẳng mặn mà cái cuộc sống hàng ngày bình dị của mình. Với kiểu người như cậu ta thì không thể có chuyện tuyên bố nhận một dàn harem trong Hội học sinh, hay mắc kẹt và phải xoay xở trong thế giới của game trực tiếp được …

“… Cậu thử cùng tớ tham gia Câu lạc bộ Game nhé?”

Thế nhưng cuộc đời cậu ta cuối cùng cũng vấp phải một diễn biến mang chiều hướng hài – tình cảm đáng kinh ngạc, khi được đệ nhất mỹ nữ của trường, đồng thời là chủ tịch Câu lạc bộ Game – Karen Tendo – bắt chuyện. Liệu đây có phải là sự khởi đầu của một rom-com với các mỹ nữ thích chơi game!?

GAMERS! – Tập 1 – Câu chuyện hài – tình cảm về tuổi thanh xuân lắm những rắc rối thoáng qua của những cô cậu game thủ tự ti, sau đây xin được phép bắt đầu!','hinh7.jpg',65,N'Sekina Aoi - Saboten '),
/* */
('SP008','2',N'Nhật Ký Quan Sát Poyo Poyo',20000,80,N'Chú mèo béo Poyo poyo đang ngày càng lợi dụng sự đáng yêu của bản thân mình để đốn tim cô chủ của nó. Và cả những bạn đọc Việt Nam

Bạn có mong chờ tập tiếp theo?

Tiếp tục theo dõi những “chiêu trò” cướp trái tim của chú mèo này nhé.','hinh8.jpg',7,N'RU TATUKI '),
/* */
('SP009','2',N'One Room Angel ',95000,100,N'Không sở thích…

Không bạn bè, không người yêu…

Mục đích sống, cũng không.

Cuộc sống của Kouki (30 tuổi), một nhân viên bán thời gian của cửa hàng tiện lợi, chỉ là một chuỗi ngày vất vưởng. Một ngày nọ, hắn sơ ý để kẻ xấu dùng dao đâm mình, trong khoảnh khắc cận kề cái chết, ý thức dần đi xa, hắn đã thấy một “thiên thần” với đôi cánh trắng toát tuyệt đẹp.

Hắn cứ ngỡ thiên thần được cử xuống đón mình, nhưng sau khi hoàn tất điều trị và trở về nhà, thiên thần lúc đó một lần nữa xuất hiện trước mặt hắn.

Kouki, với sự thương xót dành cho một thiên thần dạn dĩ, mất phương hướng và kí ức, không thể tung cánh bay lên, hắn đã quyết định cho thiên thần ở lại căn phòng trọ ọp ẹp của mình.

Cuộc sống chung dưới một mái nhà kỳ lạ bắt đầu một cách đột ngột. Chuỗi ngày sống cùng thiên thần, từ một người sống mà như chết, khiến những cung bậc cảm xúc một lần nữa nở rộ trong tim Kouki…','hinh9.jpg',12,N'Harada '),
/* */
('SP010','3',N'Đắc Nhân Tâm',300000,500,N'Đắc nhân tâm của Dale Carnegie là quyển sách nổi tiếng nhất, bán chạy nhất và có tầm ảnh hưởng nhất của mọi thời đại. Tác phẩm đã được chuyển ngữ sang hầu hết các thứ tiếng trên thế giới và có mặt ở hàng trăm quốc gia. Đây là quyển sách duy nhất về thể loại self-help liên tục đứng đầu danh mục sách bán chạy nhất (best-selling Books) do báo The New York Times bình chọn suốt 10 năm liền.
Riêng bản tiếng Anh của sách đã bán được hơn 15 triệu bản trên thế giới. Tác phẩm có sức lan toả vô cùng rộng lớn - dù bạn đi đến bất cứ đâu, bất kỳ quốc gia nào cũng đều có thể nhìn thấy. Tác phẩm được đánh giá là quyển sách đầu tiên và hay nhất, có ảnh hưởng làm thay đổi cuộc đời của hàng triệu người trên thế giới.
Không còn nữa khái niệm giới hạn Đắc Nhân Tâm là nghệ thuật thu phục lòng người, là làm cho tất cả mọi người yêu mến mình. Đắc nhân tâm và cái Tài trong mỗi người chúng ta. Đắc Nhân Tâm trong ý nghĩa đó cần được thụ đắc bằng sự hiểu rõ bản thân, thành thật với chính mình, hiểu biết và quan tâm đến những người xung quanh để nhìn ra và khơi gợi những tiềm năng ẩn khuất nơi họ, giúp họ phát triển lên một tầm cao mới. Đây chính là nghệ thuật cao nhất về con người và chính là ý nghĩa sâu sắc nhất đúc kết từ những nguyên tắc vàng của Dale Carnegie.','hinh10.jpg',100,N'Dale Carnegie'),
/* */
('SP011','4', N'Thỏ bảy màu',150000,100,
N'Thỏ bảy màu là tên một loạt truyện tranh ngắn do họa sĩ Huỳnh Thái Ngọc sáng tác từ năm 2014. Loạt truyện ban đầu thuộc dạng ngắn và được đăng tải chủ yếu trên Facebook. Đến năm 2015, tập truyện đầu tiên của Thỏ bảy màu đã được ra mắt với tên Thỏ bảy màu - Timeline của tôi có gì? do nhà xuất bản Thế giới phát hành. Tập truyện thứ hai đã được ra mắt sau đó 7 năm có tên Thỏ bảy màu và những người nghĩ nó là bạn do nhà xuất bản Dân trí phát hành.
"Nghe lời thỏ, coi như bỏ" được xem là khẩu hiệu xuyên suốt của bộ truyện.','hinh11.jpg',1200,N'Huỳnh Thái Ngọc'),

/* */
('SP012','4', N'Ngôi sao Kazan',112000,200,
N'Baree thừa hưởng từ cha mình, Kazan, sự nhạy cảm với cái đẹp và nét dịu dàng, đến mức gần như tôn thờ phụ nữ. Chút phần ít ỏi dòng máu chó trong nó luôn nhắc nó nhớ về hơi ấm của con người, của bếp lửa. Còn phần lớn dòng máu sói lại giữ cho nó bền bỉ hơn, quả cảm hơn trong những giây phút đầy bất trắc của cuộc đời. Câu chuyện về cuộc đời Baree, oái oăm thay, cũng chính là câu chuyện về những cuộc kiếm tìm Cây Liễu - cô gái chủ nhân của nó.
... Tiết trời đã vào mùa hẹn hò, mùa xây tổ mới, và Baree đang trở về nhà. Nhưng con chó lai sói không trở về theo tiếng gọi lứa đôi mà trở về vì Cây Liễu. Nó biết chắc cô đang ở đâu đây, có lẽ chỗ vách núi nơi nó nhìn thấy cô lần cuối. Ước ao trở về với cô khiến con vật vô cùng háo hức. Nó nghển đầu nhìn Carvel rồi cất tiếng sủa nhặng lên, như thể thúc giục anh phải nhanh chân hơn nữa...',
'hinh12.jpg',1700,N'James Oliver Curwood'),
/* */

('SP013','4', N'Hồ chuồn chuồn',180000,100,
N'Talitha Hamilton là con gái của bác sĩ được yêu mến của thị trấn. Tally yêu cuộc sống của cô ấy; cô ấy hòa đồng với mọi người. Sau khi Chiến tranh thế giới thứ hai bùng nổ , Tally rất buồn khi biết mình sẽ được gửi đến một trường nội trú ở Anh , Delderton, nhưng làm như vậy theo yêu cầu của cha cô. Khi Tally đến Delderton, cô nhanh chóng phát hiện ra rằng ngôi trường này không giống bất kỳ ngôi trường nào khác: giáo viên dạy khiêu vũ khuyến khích họ trở thành những hạt giống vươn tới ánh sáng, và giáo viên sinh vật học bí ẩn Matteo đưa họ đi dạo để học vào lúc 4 giờ sáng. Tally sớm ổn định cuộc sống và kết bạn với nhiều người, thậm chí còn tổ chức cho bạn bè của cô ấy một chuyến đi tham dự lễ hội khiêu vũ ở Bergania.',
'hinh13.jpg',1200,N'Eva Ibbotson'),
/* */

('SP014','4', N'Truyện kể cho bé trước giờ đi ngủ',120000,500,
N'Truyện kể cho bé trước giờ đi ngủ gồm nhứng bộ truyện vui nhộn và thú vị giúp những bé nghe trước khi ngủ dễ hơn. Sách bao gồm 101 câu truyện vui, buồn, ý nghĩa đều có giúp cho trẻ em có những bài học trong cuộc sống.',
'hinh14.jpg',1200,N'Vân Anh'),
/* */
('SP015','4', N'Ai từng là con nít',260000,700,
N'TT - Ai từng là con nít (NXB Trẻ) là bộ sách thú vị vừa được phát hành gần đây của nhóm tác giả trẻ thuộc nhóm bút Vòm Me Xanh (báo Mực Tím), như một nỗ lực đáng quý trong việc viết sách Việt cho trẻ em Việt.
Bộ sách gồm sáu tập được xếp theo các chủ đề khác nhau, kể chuyện con nít mơ, con nít ăn, con nít chơi, con nít mặc, con nít đi và con nít làm theo kiểu của con nít.
Người đọc sẽ bị cuốn hút bởi lối kể chuyện hồn nhiên, chân thật và hóm hỉnh về khung trời tuổi thơ đa màu sắc, đa văn hóa, đa vùng miền; được sống lại những cảm xúc “rất con nít” không dễ gì gặp lại khi đã là người lớn. Đó là niềm hạnh phúc lâng lâng được “bay” khi ngồi lọt thỏm trong chiếc thúng theo nhịp đòn gánh mỗi sáng mẹ ra chợ, là mùi vị ngon tuyệt của món canh riêu cua mà cua được giã trong chiếc nón sắt cũ của lính Mỹ, là cảm giác chiến thắng vô đối của cô bé con liều lĩnh mơ làm anh hùng nhảy sông, hay nỗi ngậm ngùi trẻ con khi sớm biết đến những nghẹn ngào ly biệt đầu đời... Đọc, để biết trong đời mỗi người luôn có những kỷ niệm còn hoài trong ký ức.',
'hinh15.jpg',100000,N'Nhiều tác giả'),

/* */
('SP016','4', N'Ngoan ngoãn',210000,300,
N'Khi con còn nằm trong nôi, bố mẹ chăm bẵm từng bữa sữa và giấc ngủ, chỉ mong con tự đứng tự đi được. Đến khi con đứng dậy, bắt đầu làm được việc này việc khác thì bố mẹ lại có nỗi lo khác. Làm sao để con không va vấp, làm sao con hiểu chuyện, giỏi giang, khôn lớn mỗi ngày?
Bộ sách này là cẩm nang để bố mẹ đi cùng con qua tuổi mẫu giáo, giúp con tự lập dần trong cuộc sống. Nào tự tắm rửa, rồi ăn đủ chất, giữ an toàn khi đi đường, tập cảm ơn và xin lỗi... các kỹ năng nho nhỏ sẽ giúp bé tự lập dần.
Bộ sách gồm 7 cuốn, đề cập đến 7 khía cạnh xoay quanh cuộc sống của mỗi em bé. Nào là dạy bé cách tự ngủ ngoan và chăm sóc mình hằng ngày, nào là dạy bé biết cách cảm ơn và xin lỗi, nào là để bé biết tự vệ sinh cơ thể như tắm rửa và đánh răng, nào là dạy bé chăm sóc bố mẹ khi ốm mệt, nào là dạy bé cách ăn uống đúng cách như cùng ăn sinh nhật với bạn bè, nào là dạy bé cách sẻ chia đồ chơi và đồ dùng với bạn, nào là dạy bé vui chơi ngoài thiên nhiên và vun trồng cây cối.'
,'hinh16.jpg',15000,N'Nhiều tác giả'),
/* */

('SP017','4', N'Cậu bé dòng sông',150000,500,
N'Câu chuyện này không bắt đầu với Cậu Bé Dòng Sông. Nó bắt đầu, như bao chuyện khác, với ông nội, và bơi lội.
Ông nội sắp mất. Ông yếu lắm rồi, nhưng cứ từ chối nhập viện, khăng khăng đòi về quê cũ nơi thâm sơn cùng cốc để hoàn thành tác phẩm cuối cùng của đời cầm cọ. Jess không tài nào hiểu nổi, cho đến khi chính cô bé bị cuốn vào những sự vụ kì dị xoay quanh bức họa lạ lùng đó. Rồi Jess gặp cậu ấy. Cậu Bé Dòng Sông. Và cô bé cần, thật nhanh, vén bức màn bí mật và vượt qua thử thách dành cho chính mình – trước khi quá muộn...
“Cậu Bé Dòng Sông hội tụ đầy đủ yếu tố làm nên một tác phẩm văn học thiếu nhi kinh điển.” - GIÁM KHẢO HUÂN CHƯƠNG CARNEGIE
Cậu Bé Dòng Sông giành được Huân chương Carnegie năm 1997 và giải Angus Book Award năm 1999 – hai giải thưởng uy tín của nước Anh trao cho các tác phẩm văn học xuất sắc dành cho độc giả trẻ.'
,'hinh17.jpg',10200,N'Tim Bowler'),
/* */

('SP018','4', N'Đôi mắt của rừng',225000,600,
N'Mặt trời đã lên cao. Sương dần tan. Đứng bên bờ hồ, có thể phóng tầm mắt ra xa hơn những ngọn đồi, thấy núi trập trùng bao phủ màu xanh của rừng. Làn sương mờ mỏng vương vấn. Đó đây, những đám mây sà thấp xuống, chạm vào đỉnh núi. Từ trên cao nhìn xuống, những ngôi nhà gỗ mái ngói xám nâu xen lẫn đỏ sậm, nằm bình lặng dưới chân đồi. Làn khói bếp bay lên từ mái nhà xa xăm...
Tuổi thơ Tây Nguyên mở ra qua những dòng văn bềnh bồng, mộc mạc, đưa ta theo chân Hậu, Sóc, Tùng… những đứa trẻ vùng cao nguyên trong trẻo, hồn hậu, cùng khám phá vẻ đẹp và những điều bí mật của đại ngàn.'
,'hinh18.jpg',13000,N'Nguyễn Vân Anh'),
/* */

('SP019','5', N'Lãnh đạo toàn năng',150000,700,
N'Lãnh đạo là năng lực tạo dựng môi trường làm việc trong đó mỗi thành viên thấu hiểu những kỳ vọng của tổ chức đặt trên vai mình và cảm thấy hoàn toàn tự nguyện khi chung sức đồng lòng gánh vác công việc nhằm đạt được kết quả tốt nhất. Từ lãnh đạo bản thân đến lãnh đạo nhóm, tổ chức đều có thể rèn luyện mà giỏi.
Cuốn sách cũng sẽ gợi mở một số kinh nghiệm thực tiễn, thiết thực nhất giúp bạn phát triển các khía cạnh của năng lực lãnh đạo trong vai trò cá nhân, đồng thời khuyến khích năng lực lãnh đạo và sáng kiến từ mọi thành viên trong đội ngũ.'
,'hinh19.jpg',12000,N'Nhiều tác giả'),
/* */

('SP020','5', N'Đại bàng tái sinh',120000,7000,
N'“Đại bàng tái sinh” Dành cho mọi đối tượng độc giả yêu thích các tác phẩm văn học thể loại phiêu lưu.
Loài đại bàng luôn tin vào một huyền thoại, rằng khi già đi, đại bàng có thể lột xác, trở thành một đại bàng trẻ trung kiêu hùng như xưa. Là một đại bàng choai, Đại Bàng Trắng rất kiêu ngạo và tự hào mình là chúa tể của bầu trời. Vì tin vào khả năng tái sinh nên Đại Bàng Trắng càng tự mãn, coi thường tất thảy, ngày ngày trêu đùa các con vật nhỏ, khiêu khích, tranh đua với các con vật khác như Báo Gấm, Cá Buồm… Đại Bàng Trắng rất yêu thích các cơn bão, thường trêu đùa cùng những cơn giông gió và sấm chớp. Nhưng rồi, vào Đại Bàng lại gặp nạn. Nó bị thương ở chân và ở cánh, không thể bay được nữa. Sẻ Nâu và Rùa Núi đã dồn hết sức lực đưa Đại Bàng vào một hang đá để chăm sóc.'
,'hinh20.jpg',10200,N'Phạm Thị Thanh Hà'),
/* */

('SP021','5', N'Napoleon Đại Đế',302000,1000,
N'Napoleon là một nhân vật đặc biệt vĩ đại và hấp dẫn trong lịch sử Pháp cũng như lịch sử thế giới. Cuộc đời, sự nghiệp, quan điểm, tài năng của ông đã là chủ đề của hàng nghìn cuốn sách trong suốt hai thế kỉ qua, và có lẽ sẽ còn được nghiên cứu tiếp trong nhiều thế kỉ sau nữa.
Trong bối cảnh những cuốn sách dịch ra tiếng Việt về ông cũng như về thời hậu Cách mạng Pháp ở Việt Nam vẫn chỉ đếm trên đầu ngón tay, ít cả về số lượng lẫn tầm vóc, thì cuốn sách Napoleon Đại Đế có thể xem là một hiện tượng bởi tầm vóc, quy mô, tính chất giàu sử liệu đã làm nên giá trị đặc biệt của tác phẩm.
Tác giả Andrew Roberts, trong cuốn tiểu sử rất đồ sộ này đã đi sâu mô tả một cách chi tiết các hoàn cảnh của thời Cách mạng Pháp đã đưa Napoleon lên đỉnh quyền lực, cách mà ông đã giành lấy nó bằng đại bác và lưỡi lê, và củng cố quyền lực bằng chiến tranh, đầu tiên với những người Jacobin, sau đó là dẹp loạn Vandée, và cuối cùng đem chiến tranh ra ngoài biên giới nước Pháp để trở thành chúa tể của Châu Âu lục địa trong một cuộc chiến mù quáng chống lại nước Anh nhằm thiết lập một trật tự thế giới do Paris điều khiển.'
,'hinh21.jpg',322200,N'Don M. Green'),
/* */

('SP022','5', N'100+ Chỉ Số Xây Dựng KPI Cho Doanh Nghiệp',125000,300,
N'100+ Chỉ Số Xây Dựng KPI Cho Doanh Nghiệp là phiên bản đầy đủ, chi tiết, thực tế nhất về tất cả mọi chỉ số cần xây dựng trong KPI cho doanh nghiệp.
Có đến 80% doanh nghiệp đưa ra các chỉ số KPI cho có, không mang lại hiệu quả thúc đẩy hiệu quả công việc và gia tăng doanh số. Lý do bởi lãnh đạo, quản lý hoặc HR chưa hiểu đúng bản chất của KPI, và mỗi bộ phận phòng ban sẽ có cách xây dựng KPI riêng khi áp dụng sẽ khá phức tạp. 100+ Chỉ Số Xây Dựng KPI Cho Doanh Nghiệp sẽ là cuốn cẩm nang đầy đủ, dễ hiểu nhất để áp dụng KPI cho tất cả các mô hình doanh nghiệp. Nếu sử dụng KPIs như là công cụ để ràng buộc hiệu suất, phân chia lương thưởng thì việc ứng dụng KPI đã sai ngay khi chưa bắt đầu.'
,'hinh22.jpg',22200,N'Tsyoshi Shimada'),
/* */

('SP023','5', N'Thành Bại Tại Chốt Sales',152000,600,
N'Để kết thúc thành công những thương vụ bán hàng
Bạn có đang gặp khó khăn trong chốt sales?
Như bạn đã biết, hiện nay nền kinh tế thì đang phát triển và trên thị trường ngày càng có rất nhiều mô hình kinh doanh, sản phẩm, hàng hóa và dịch vụ vô cùng đa dạng. Đối tượng phục vụ cũng rất rộng rãi, bao gồm các tầng lớp trong xã hội từ các thành phố, đô thị sầm uất đến tận những vùng sâu, vùng xa.
Đi kèm với điều này thì ngành tư vấn và bán hàng cũng đang ngày càng phát triển mạnh mẽ và bùng nổ. Hiện nay, đây là một trong những lĩnh vực rất hấp dẫn và tạo ra nguồn thu nhập cao.
Tuy nhiên, tư vấn và bán hàng là những lĩnh vực chưa bao giờ dễ dàng cả. Hiện nay nhiều người bán hàng đang gặp rất nhiều khó khăn và vất vả trong quá trình làm nghề. Hầu hết những khó khăn của họ không đến từ việc tìm kiếm khách hàng, lên cuộc hẹn hay gặp gỡ và tư vấn cho khách hàng mà là đến từ việc chốt sales. Đúng vậy, mặc dù họ đã làm rất tốt ở các bước trước đó, nhưng đến lúc cần chốt sale thì phần đông họ lại không làm hiệu quả.'
,'hinh23.jpg',29220,N'Jack Phan'),
/* */

('SP024','5', N'Smarketing - Giải Pháp Cạnh Tranh',234000,300,
N'Từ trước tới nay, bán hàng và marketing luôn là hai đế chế riêng biệt, với bộ máy lãnh đạo riêng, ngân sách riêng và cấu trúc tổ chức riêng.
Giờ đây, trước những thay đổi không ngừng của thế giới kinh doanh, với sự bùng nổ của internet và hiểu biết của khách hàng ngày một cao, tương lai của các doanh nghiệp phụ thuộc vào việc phát triển hai đế chế này theo kịp với sự thay đổi của thế giới.
Vì vậy, hai bộ phận này đã được hợp nhất thành đơn vị duy nhất, gọi là Smarketing. Mục tiêu chính của Smarketing là nâng cao chất lượng trải nghiệm khách hàng thông qua sắp xếp hợp lý chu trình tương tác với khách hàng; đưa ra những số liệu thống nhất giữa hai bộ phận; cung cấp cho ban lãnh đạo cái nhìn toàn cảnh về các chỉ số đo lường kết quả bán hàng...
Cuốn sách này sẽ cung cấp phương pháp, quy trình và các công cụ cần thiết để các công ty chuyển đổi sang mô hình vận hành dựa trên Smarketing, mà sự phát triển của nó sẽ quyết định tương lai của doanh nghiệp.'
,'hinh24.jpg',23200, N'Tim Hughes'),
/* */

('SP025','5', N'Bí Mật Quản Trị Nhân Lực',132000,500,
N'Quản trị nguồn nhân lực suy cho cùng là phục vụ sứ mệnh doanh nghiệp, bởi nếu như nhân viên thiếu cảm giác đồng lòng cần có đối với doanh nghiệp, như vậy họ rất khó có niềm say mê để phát huy hết tài năng và trí tuệ bản thân!
Cuốn sách Bí mật quản trị nhân lực: Cách tạo ra một đội quân bách chiến bách thắng sẽ giúp bạn hiểu rằng muốn quản lý tốt nguồn nhân lực, trước tiên nên bắt đầu từ hiểu rõ và nhớ rõ sứ mệnh doanh nghiệp nếu không sẽ chỉ là máy móc rập khuôn, nhìn mèo vẽ hổ. Thấm sâu trong từng trang sách là một trục tư tưởng giá trị quan xuyên suốt về cách dùng người mà độc giả có thể học hỏi từ “đế chế” Alibaba.'
,'hinh25.jpg',32200,N'Trần Vĩ'),
/* */

('SP026','5', N'Business Coaching',320000,700,
N'Tôi thực sự nể phục những người thừa kế 1 tỷ mà tạo ra 1000 tỷ nhưng tôi còn nể phục hơn những người làm nên 1000 tỷ mà họ bắt đầu từ con số 0. Với những tấm gương từ hàng trăm doanh nhân và doanh nghiệp nổi tiếng trên thế giới Bizbooks mang đến cho bạn cuốn sách bom tấn  “Business Coaching - Đế chế kinh doanh từ con số 0” để giúp các nhà lãnh đạo có thể tạo nên một đế chế kinh doanh vững mạnh từ hai bàn tay trắng chỉ cần bạn biết mình thực sự đang làm gì. Ở đây bạn sẽ học hỏi được những kỹ năng và những nguyên tắc kinh doanh mà không phải bất cứ nhà quản trị nào cũng có được.
Với hàng trăm ví dụ cụ thể, sinh động, sắp xếp trong một khuôn mẫu rõ ràng, nhất quán. Cuốn sách sẽ là một công cụ đắc lực tạo điều kiện dễ dàng áp dụng cho các nhà quản trị và nhà kinh doanh ở mọi cấp độ khác nhau. “Business Coaching - Xây dựng đế chế kinh doanh từ con số 0”. Đây thực sự sẽ là một bản kế hoạch chi tiết cho việc xây dựng doanh nghiệp phát triển thành công  và bền vững trong thế kỷ XXI. '
,'hinh26.jpg',123200,N'Jack Welch'),
/* */

('SP027','6', N'Dinh Dưỡng 4.0',150000,500,
N'rên thực tế có rất nhiều yếu tố ảnh hưởng đến sức khỏe của chúng ta. Có những thứ không thể thay đổi như tuổi tác hoặc yếu tố di truyền; và những thứ có thể thay đổi, chẳng hạn như cách ta lựa chọn đồ ăn, thức uống trong cuộc sống hằng ngày. Thực phẩm chúng ta ăn là hỗn hợp của nhiều chất dinh dưỡng và cơ thể khỏe mạnh hay không cũng xuất phát từ chế độ dinh dưỡng ta lựa chọn.
Trong thời đại ngày nay, ai ai cũng muốn mình có một thân hình thật đẹp thật khỏe và chúng ta sẽ thường tìm đến các phương pháp giảm cân, thay đổi lại chế độ ăn uống, sử dụng các loại trà bánh nước không đường,... Như vậy, chắc hẳn rằng hầu hết chúng ta đã từng ít nhất một lần nghe đến những chế độ ăn lành mạnh như ăn Keto, ăn Địa Trung Hải, ăn Chay,... đi kèm với vô vàn những lời khuyên '
,'hinh27.jpg',23200,N'Tim Spector'),
/* */

('SP028','6', N'Những Người Nhật Tử Tế',135000,300,
N'Những Người Nhật Tử Tế là tuyển tập những mẩu truyện ngắn được tác giả viết từ năm 2011 đến 2016. Chủ đề và tình tiết thiên về những câu chuyện bí ẩn, rất đời thường, được lấy cảm hứng từ bốn mùa của nước Nhật, và dĩ nhiên là cái kết luôn đầy bất ngờ, cô đọng chất Keigo Higashino.
Không chỉ xây dựng được câu chuyện gay cấn với những tình tiết lôi cuốn, Keigo Higashino còn rất giỏi khơi gợi sự kịch tính trong đời sống thường nhật và khéo léo biến chúng thành văn chương qua giọng kể hấp dẫn, khiến độc giả dễ dàng bị cuốn theo nhịp truyện, và phải ngơ ngác xen lẫn ngỡ ngàng trước cái kết không ngờ đến.
Chùm truyện ngắn nằm trong tuyển tập Những người Nhật tử tế đem đến cho người đọc cảm giác giản dị “thuần Nhật”, nhưng cũng lại như một lăng kính vạn hoa ma quái, chiếu nên một góc Nhật Bản rất khác biệt, rất thú vị so với những gì ta biết.'
,'hinh28.jpg',12200,N'Kiego Higashino'),
/* */

('SP029','6', N'Ăn Uống Nói Cười & Khóc',153000,800,
N'Đây là tập sách biên khảo về hoạt động của cái miệng trên khuôn mặt con người. Thông qua các hoạt động Ăn, Uống, Nói, Cười và Khóc, tác giả dẫn trích những thuật ngữ, từ ngữ, thành ngữ, tục ngữ liên quan đến các hoạt động đó của con người Việt Nam.
Có thể nói đây là một công trình khá thú vị trên một số hoạt động của các miệng người ta thường ngày tưởng vô cùng đơn giản nhưng cực kỳ phong phú, đa dạng và súc tích. Từ đó ta càng hiểu hơn tại sao ông bà ta thường dạy muốn làm người bình thường, phải “học ăn, học nói, học gói, học mở”. Trong 4 cái cần phải học đó, 2 cái học đã liên quan đến hoạt động của cái miệng con người rồi.'
,'hinh29.jpg',11200,N'Trần Huiền Ân'),
/* */

('SP030','6', N'Bột Nước Muối Men',235000,400,
N'Cho dù bạn là thợ làm bánh chuyên nghiệp hay mới chỉ chập chững bước đi đầu tiên, BỘT NƯỚC MUỐI MEN đều có công thức và thời gian nấu nướng phù hợp với bạn, góp phần đưa việc nấu nướng của bạn lên một tầm cao mới.
BỘT NƯỚC MUỐI MEN không chỉ là bộ sưu tập các công thức làm bánh mì và pizza thủ công đơn thuần, mà còn cung cấp cho bạn đọc một khóa học làm bánh hoàn chỉnh và một vài thông tin thú vị khác về làm bánh, cùng những hướng dẫn và giải thích, cũng như mẹo làm bánh của bếp trưởng Ken Forkish.
BỘT NƯỚC MUỐI MEN là cẩm nang không thể thiếu dành cho những thợ làm bánh mong muốn những  mẻ bánh của họ thật đặc biệt và xứng tầm!'
,'hinh30.jpg',23200,N'Ken Forkish'),
/* */
('SP031','6', N'Bạn Là Những Gì Bạn Ăn',125000,300,
N'“Bạn Là Những Gì Bạn Ăn” - là cẩm nang dinh dưỡng tuyệt vời của Liana đem đến cho bạn sự hiểu biết tất tần tật về nguồn gốc của lo âu thông qua việc ăn uống, những thực phẩm chống và loại bỏ sự lo lắng, các công thức nấu ăn xua tan mệt mỏi, các chiến lược vô cùng hiệu quả để bảo vệ hệ thần kinh…
Với cuốn sách này, chúng ta sẽ có thể thoải mái tận hưởng cuộc sống dù thời đại này đôi lúc thật khó khăn. Những cuộc chiến không còn xảy ra bên ngoài mà bên từ bên trong, ngay ở trái tim, tâm trí và cơ thể. Nhưng khi hiểu biết được về cơ chế dinh dưỡng và nắm bắt được tầm quan trọng của việc ăn uống đối với đời sống, chúng ta sẽ có thể cải thiện cuộc đời của mình một cách ít tốn kém và hiệu quả hơn. Cuộc chiến tinh thần sẽ chấm dứt, từ đây với “Bạn Là Những Gì Bạn Ăn”, bạn sẽ có thể trải nghiệm cuộc đời mình một cách đầy phóng khoáng, tự tin và tràn ngập hạnh phúc.'
,'hinh31.jpg',12200,N'Liana Werner'),
/* */
('SP032','6', N'Bản Giao Hưởng Cuộc Sống',322000,200,
N'Lúc bạn lênh đênh trên dòng đời, những người đồng trang lứa đều đã tiến thân, lương nhiều con số. Bạn trở về. Như một người đi sau. Nhưng một người bị bỏ lại. Nhưng bạn hài lòng. Vì những trải nghiệm đó chưa bao giờ ngưng khiến bạn tự hào về bản thân, cho dù là hôm nay, ngày mai hay nhiều ngày sau nữa. Rất nhiều cánh cửa đã đóng lại. Nhưng cũng có rất nhiều cánh cửa khác mở ra. Bạn còn trẻ. Đó là một đặc ân. Nhưng chớ cho rằng mọi đIều tốt đẹp đều ngẫu nhiên xảy đến. Bạn nghĩ thế khi bắt đầu gõ những dòng đầu tiên trong cuốn sách này.'
,'hinh32.jpg',21100,N'Nhiều tác giả')
/* */
/* */
GO

INSERT INTO dbo.Users VALUES
(N'Adminid102','$2a$10$2wTMQzDKzkfmhpzPAM32AuAmqCBIr6UNOpOQjUtnPWqhaA5177Ugy',N'Admin',N'admin129@gmail.com', 1, '2003-08-23',N'0933969627',N'Cần Thơ', 1),
(N'User','$2a$10$c26HmHKDMup6jjOmMuCdqe3Vh8eFsbk5C6C1XWn7UBDAchlHy/RFC',N'User',N'user123@gmail.com', 1, '2003-08-23',N'0932169627',N'Cần Thơ', 1),
(N'haydt','$2a$10$jlA6yo8S5UYbXOf0pxtGve2e1Sjf2T2YYE8lqe8EyhUWLTCVPKlQC',N'Dư Trường Hay',N'hay123@gmail.com', 1, '2003-11-22',N'094321154',N'Cần Thơ', 1),
(N'thinh123','$2a$10$KSKo.sJridAKz4KmBy4ds.mISbxp08UHcSTKQFz8lx7tJFhzr0qRG',N'Nguyễn Hưng Thịnh',N'thinh123@gmail.com', 1, '2003-12-23',N'0933969637',N'Cần Thơ', 1),
(N'trong123','$2a$10$ZDQSJRyWLCimRrUqCBa/oegmRWKZNiC8HttWnA7tzl6tgmFfFg3m6',N'Lê Lâm Quốc Trọng',N'trong123@gmail.com', 1, '2003-08-13',N'0932132154',N'Cần Thơ', 1),
(N'datvt','$2a$10$zLSMUPoFSUSMXE.NyrnmpOwLxRVeBhl/uxE/oIARCeu/4iwzR5b0W',N'Vạng Thành Đạt',N'dat123@gmail.com', 1, '2003-08-23',N'0923122154',N'Cần Thơ', 1),
(N'ha123','$2a$10$ZOEpNpkyFhgcjmnyMOIPcu5lafk7wUHGji9Przx0XwSKJPTVwIWrS',N'Nguyễn Thanh Hà',N'ha123@gmail.com', 0, '2005-12-22',N'091341554',N'Cần Thơ', 1),
(N'ducngo12','$2a$10$014nakee17qYKQlxFDjT2uiVUL61dTWGEGXOTLFDJCIn/6xQAdOE6',N'Nguyễn Đức Ngô',N'ngoduc123@gmail.com', 1, '2002-08-23',N'0923939612',N'Cần Thơ', 1),
(N'tuananh12','$2a$10$uL2eZf1la834ycEvTt3OR.U..NyrqE5WDjziCE1LibPcxLBr/CVu.',N'Lê Tuấn Anh',N'tuananh123@gmail.com', 1, '2003-03-13',N'0912133254',N'Cần Thơ', 1),
(N'hien123','$2a$10$wjtIhfiWqQF3oMDMLRxm5uroEEpunydNHII7XMzkYh98rG1NtNYm6',N'Vạng Đức Hiển',N'hienne123@gmail.com', 1, '2001-09-13',N'0933112351',N'Cần Thơ', 1),
(N'tuankiet12','$2a$10$yLl4ILuc/BeIDxR5VzD/dOFr3RN2.xp.fmi3aRmySbpRoWCHdHH1K',N'Lê Tuấn Kiệt',N'tuankiet123@gmail.com', 1, '2001-03-13',N'0931231214',N'Cần Thơ', 1),
(N'nhitt','$2a$10$kdtUCVse6DsTQUg4FWLtDOeXpArBmi.HKjggTc34PYAPn9BSV1SqK',N'Trần Tuyết Nhi',N'nhi789@gmail.com', 0, '2003-03-13',N'0989997998',N'Cần Thơ', 1),
(N'admin_api','$2a$10$89plZA/8yzdyNnK3Wbgct.rmGc5EdBBH.yo40LetLqIonWPGMrOwW',N'Admin API', 'admin@123', 1, '2000-01-01',N'0999999999',N'Cần Thơ', 1)
GO

INSERT INTO dbo.Roles
VALUES('ADMIN', 'Administor'),
('USER', 'User default'),
( 'ADMIN_API', 'Admin API')
GO

INSERT INTO dbo.Authorities
VALUES('haydt', 'ADMIN'),
('haydt', 'USER'),
('datvt', 'USER'),
('nhitt', 'USER'),
('nhitt', 'ADMIN'),
('tuankiet12', 'USER'),
('admin_api', 'ADMIN_API'),
('admin_api', 'USER'),
('admin_api', 'ADMIN'),
('tuankiet12', 'USER'),
('hien123', 'USER'),
('tuananh12', 'USER'),
('ducngo12', 'USER'),
('ha123', 'USER'),
('trong123', 'USER'),
('thinh123', 'USER'),
('User', 'USER'),
('trong123', 'ADMIN'),
('thinh123', 'ADMIN'),
('Adminid102', 'ADMIN')
GO

INSERT INTO dbo.Carts VALUES 
('haydt', 'SP001', 1),
('haydt', 'SP013', 1),
('haydt', 'SP023', 3),
('haydt', 'SP025', 1),
('thinh123', 'SP013', 2),
('thinh123', 'SP015', 1),
('trong123', 'SP013', 3),
('trong123', 'SP021', 1),
('trong123', 'SP028', 1),
('datvt', 'SP013', 1),
('datvt', 'SP018', 1),
('datvt', 'SP023', 1),
('ha123', 'SP012', 1),
('ha123', 'SP013', 1),
('ha123', 'SP015', 1),
('ha123', 'SP018', 1),
('ha123', 'SP021', 1),
('ducngo12', 'SP016', 1),
('ducngo12', 'SP017', 1),
('ducngo12', 'SP018', 1),
('ducngo12', 'SP011', 1),
('ducngo12', 'SP022', 1),
('tuananh12', 'SP029', 1),
('tuananh12', 'SP013', 1),
('tuananh12', 'SP028', 1),
('tuananh12', 'SP021', 1),
('hien123', 'SP030', 1),
('hien123', 'SP023', 1),
('hien123', 'SP028', 1), 
('hien123', 'SP026', 1),
('tuankiet12', 'SP011', 1),
('tuankiet12', 'SP013', 1),
('tuankiet12', 'SP015', 1),
('tuankiet12', 'SP018', 1),
('tuankiet12', 'SP021', 1),
('tuankiet12', 'SP023', 1), 
('tuankiet12', 'SP026', 1)



INSERT INTO dbo.Orders VALUES 
('DH001', 'haydt', 888000, '2023-06-10',N'Cần Thơ', 1),
('DH002', 'thinh123', 700000, '2023-06-10',N'Cần Thơ', 1),
('DH003', 'trong123', 1262000, '2023-06-10',N'Cần Thơ', 1),
('DH004', 'datvt', 557000, '2023-06-10',N'Cần Thơ', 1),
('DH005', 'ha123', 1079000, '2023-06-10',N'Cần Thơ', 1),
('DH006', 'ducngo12', 860000, '2023-06-10',N'Cần Thơ', 1),
('DH007', 'tuananh12', 752000, '2023-06-10',N'Cần Thơ', 1),
('DH008', 'hien123', 842000, '2023-06-10',N'Cần Thơ', 2),
('DH009', 'hien123', 1117000, '2023-06-10',N'Cần Thơ', 0)


INSERT INTO dbo.OrderDetail VALUES 
('HD001', 'DH001', 'SP001', 1, '2023-06-10'),
('HD002', 'DH001', 'SP013', 1, '2023-06-10'),
('HD003', 'DH001', 'SP023', 3, '2023-06-10'),
('HD004', 'DH001', 'SP025', 1, '2023-06-10'),
('HD005', 'DH002', 'SP015', 2, '2023-06-10'),
('HD006', 'DH002', 'SP013', 1, '2023-06-10'),
('HD007', 'DH003', 'SP015', 3, '2023-06-10'),
('HD008', 'DH003', 'SP013', 1, '2023-06-10'),
('HD009', 'DH003', 'SP021', 1, '2023-06-10'),
('HD010', 'DH004', 'SP013', 1, '2023-06-10'),
('HD011', 'DH004', 'SP018', 1, '2023-06-10'),
('HD012', 'DH004', 'SP023', 1, '2023-06-10'),
('HD013', 'DH005', 'SP012', 1, '2023-06-10'),
('HD014', 'DH005', 'SP013', 1, '2023-06-10'),
('HD015', 'DH005', 'SP015', 1, '2023-06-10'),
('HD016', 'DH005', 'SP018', 1, '2023-06-10'),
('HD017', 'DH005', 'SP021', 1, '2023-06-10'),
('HD018', 'DH006', 'SP016', 1, '2023-06-10'),
('HD019', 'DH006', 'SP017', 1, '2023-06-10'),
('HD020', 'DH006', 'SP018', 1, '2023-06-10'),
('HD021', 'DH006', 'SP011', 1, '2023-06-10'),
('HD022', 'DH006', 'SP022', 1, '2023-06-10'),
('HD023', 'DH007', 'SP028', 1, '2023-06-10'),
('HD024', 'DH007', 'SP013', 1, '2023-06-10'),
('HD025', 'DH007', 'SP028', 1, '2023-06-10'),
('HD026', 'DH007', 'SP021', 1, '2023-06-10'),
('HD027', 'DH008', 'SP030', 1, '2023-06-10'),
('HD028', 'DH008', 'SP023', 1, '2023-06-10'),
('HD029', 'DH008', 'SP028', 1, '2023-06-10'),
('HD030', 'DH008', 'SP026', 1, '2023-06-10'),
('HD031', 'DH009', 'SP011', 1, '2023-06-10'),
('HD032', 'DH009', 'SP013', 1, '2023-06-10'),
('HD033', 'DH009', 'SP015', 1, '2023-06-10'),
('HD034', 'DH009', 'SP018', 1, '2023-06-10'),   
('HD035', 'DH009', 'SP021', 1, '2023-06-10'),
('HD036', 'DH009', 'SP023', 1, '2023-06-10'),
('HD037', 'DH009', 'SP026', 1, '2023-06-10')

INSERT INTO dbo.Favorites VALUES 
('haydt', 'SP001', '2023-06-10'),
('haydt', 'SP031', '2023-06-10'),
('haydt', 'SP012', '2023-06-10'),
('haydt', 'SP025', '2023-06-10'),
('thinh123', 'SP001', '2023-06-10'),
('thinh123', 'SP004', '2023-06-10'),
('thinh123', 'SP016', '2023-06-10'),
('thinh123', 'SP012', '2023-06-10'),
('trong123', 'SP012', '2023-06-10'),
('trong123', 'SP031', '2023-06-10'),
('trong123', 'SP032', '2023-06-10'),
('trong123', 'SP021', '2023-06-10'),
('datvt', 'SP031', '2023-06-10'),
('datvt', 'SP001', '2023-06-10'),
('datvt', 'SP021', '2023-06-10'),
('datvt', 'SP028', '2023-06-10'),
('ha123', 'SP012', '2023-06-10'),
('ha123', 'SP031', '2023-06-10'),
('ha123', 'SP032', '2023-06-10'),
('ha123', 'SP021', '2023-06-10'),
('ducngo12', 'SP031', '2023-06-10'),
('ducngo12', 'SP001', '2023-06-10'),
('ducngo12', 'SP021', '2023-06-10'),
('ducngo12', 'SP028', '2023-06-10'),
('tuananh12', 'SP012', '2023-06-10'),
('tuananh12', 'SP031', '2023-06-10'),
('tuananh12', 'SP032', '2023-06-10'),
('tuananh12', 'SP021', '2023-06-10'),
('hien123', 'SP031', '2023-06-10'),
('hien123', 'SP001', '2023-06-10'),
('hien123', 'SP021', '2023-06-10'),    
('hien123', 'SP028', '2023-06-10'),
('tuankiet12', 'SP001', '2023-06-10'),
('tuankiet12', 'SP002', '2023-06-10'),
('tuankiet12', 'SP007', '2023-06-10'),
('tuankiet12', 'SP011', '2023-06-10'),
('tuankiet12', 'SP015', '2023-06-10'),
('tuankiet12', 'SP017', '2023-06-10'),
('tuankiet12', 'SP021', '2023-06-10'),    
('tuankiet12', 'SP028', '2023-06-10')



