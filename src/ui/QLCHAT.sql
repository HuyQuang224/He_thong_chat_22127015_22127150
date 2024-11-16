USE [master]
GO
/****** Object:  Database [Users]    Script Date: 11/16/2024 2:44:41 PM ******/
CREATE DATABASE [Users]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'Users', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL16.SQLEXPRESS\MSSQL\DATA\Users.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N'Users_log', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL16.SQLEXPRESS\MSSQL\DATA\Users_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
 WITH CATALOG_COLLATION = DATABASE_DEFAULT, LEDGER = OFF
GO
ALTER DATABASE [Users] SET COMPATIBILITY_LEVEL = 160
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [Users].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [Users] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [Users] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [Users] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [Users] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [Users] SET ARITHABORT OFF 
GO
ALTER DATABASE [Users] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [Users] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [Users] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [Users] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [Users] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [Users] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [Users] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [Users] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [Users] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [Users] SET  DISABLE_BROKER 
GO
ALTER DATABASE [Users] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [Users] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [Users] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [Users] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [Users] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [Users] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [Users] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [Users] SET RECOVERY SIMPLE 
GO
ALTER DATABASE [Users] SET  MULTI_USER 
GO
ALTER DATABASE [Users] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [Users] SET DB_CHAINING OFF 
GO
ALTER DATABASE [Users] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [Users] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [Users] SET DELAYED_DURABILITY = DISABLED 
GO
ALTER DATABASE [Users] SET ACCELERATED_DATABASE_RECOVERY = OFF  
GO
ALTER DATABASE [Users] SET QUERY_STORE = ON
GO
ALTER DATABASE [Users] SET QUERY_STORE (OPERATION_MODE = READ_WRITE, CLEANUP_POLICY = (STALE_QUERY_THRESHOLD_DAYS = 30), DATA_FLUSH_INTERVAL_SECONDS = 900, INTERVAL_LENGTH_MINUTES = 60, MAX_STORAGE_SIZE_MB = 1000, QUERY_CAPTURE_MODE = AUTO, SIZE_BASED_CLEANUP_MODE = AUTO, MAX_PLANS_PER_QUERY = 200, WAIT_STATS_CAPTURE_MODE = ON)
GO
USE [Users]
GO
/****** Object:  Table [dbo].[Activities]    Script Date: 11/16/2024 2:44:41 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Activities](
	[activity_id] [int] NOT NULL,
	[user_id] [int] NULL,
	[action] [nvarchar](50) NULL,
	[target_id] [int] NULL,
	[created_at] [date] NULL,
 CONSTRAINT [PK_Activities] PRIMARY KEY CLUSTERED 
(
	[activity_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Friends]    Script Date: 11/16/2024 2:44:41 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Friends](
	[friendship_id] [int] NOT NULL,
	[user_id] [int] NULL,
	[friend_id] [int] NULL,
	[status] [varchar](50) NULL,
	[created_at] [date] NULL,
 CONSTRAINT [PK_Friends] PRIMARY KEY CLUSTERED 
(
	[friendship_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[GroupMembers]    Script Date: 11/16/2024 2:44:41 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[GroupMembers](
	[membership_id] [int] NOT NULL,
	[group_id] [int] NULL,
	[user_id] [int] NULL,
	[is_admin] [bit] NULL,
	[joined_at] [date] NULL,
 CONSTRAINT [PK_GroupMembers] PRIMARY KEY CLUSTERED 
(
	[membership_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Groups]    Script Date: 11/16/2024 2:44:41 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Groups](
	[group_id] [int] NOT NULL,
	[group_name] [nvarchar](100) NULL,
	[is_encrypted] [bit] NULL,
	[created_by] [int] NULL,
	[created_at] [date] NULL,
 CONSTRAINT [PK_Groups] PRIMARY KEY CLUSTERED 
(
	[group_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[LoginHistory]    Script Date: 11/16/2024 2:44:41 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[LoginHistory](
	[login_id] [int] NOT NULL,
	[user_id] [int] NULL,
	[login_time] [date] NULL,
 CONSTRAINT [PK_LoginHistory] PRIMARY KEY CLUSTERED 
(
	[login_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Messages]    Script Date: 11/16/2024 2:44:41 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Messages](
	[message_id] [int] NOT NULL,
	[sender_id] [int] NULL,
	[receiver_id] [int] NULL,
	[content] [nvarchar](1000) NULL,
	[status] [varchar](50) NULL,
	[created_at] [date] NULL,
	[is_deleted] [bit] NULL,
 CONSTRAINT [PK_Messages] PRIMARY KEY CLUSTERED 
(
	[message_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[SpamReports]    Script Date: 11/16/2024 2:44:41 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[SpamReports](
	[report_id] [int] NOT NULL,
	[reported_by] [int] NULL,
	[reported_user] [int] NULL,
	[message_id] [int] NULL,
	[created_at] [date] NULL,
 CONSTRAINT [PK_SpamReports] PRIMARY KEY CLUSTERED 
(
	[report_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Statistics]    Script Date: 11/16/2024 2:44:41 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Statistics](
	[stat_id] [int] NOT NULL,
	[year] [int] NULL,
	[month] [int] NULL,
	[new_users] [int] NULL,
	[active_users] [int] NULL,
 CONSTRAINT [PK_Statistics] PRIMARY KEY CLUSTERED 
(
	[stat_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Users]    Script Date: 11/16/2024 2:44:41 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Users](
	[user_id] [int] NOT NULL,
	[username] [varchar](50) NULL,
	[password] [varchar](255) NULL,
	[name] [nvarchar](100) NULL,
	[address] [varchar](255) NULL,
	[birth_date] [date] NULL,
	[gender] [nvarchar](50) NULL,
	[email] [varchar](100) NULL,
	[status] [varchar](50) NULL,
	[created_at] [date] NULL,
	[updated_at] [date] NULL,
 CONSTRAINT [PK_Users] PRIMARY KEY CLUSTERED 
(
	[user_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
ALTER TABLE [dbo].[Activities]  WITH CHECK ADD  CONSTRAINT [FK_Activities_Users] FOREIGN KEY([user_id])
REFERENCES [dbo].[Users] ([user_id])
GO
ALTER TABLE [dbo].[Activities] CHECK CONSTRAINT [FK_Activities_Users]
GO
ALTER TABLE [dbo].[Friends]  WITH CHECK ADD  CONSTRAINT [FK_Friends_Users] FOREIGN KEY([user_id])
REFERENCES [dbo].[Users] ([user_id])
GO
ALTER TABLE [dbo].[Friends] CHECK CONSTRAINT [FK_Friends_Users]
GO
ALTER TABLE [dbo].[Friends]  WITH CHECK ADD  CONSTRAINT [FK_Friends_Users1] FOREIGN KEY([friend_id])
REFERENCES [dbo].[Users] ([user_id])
GO
ALTER TABLE [dbo].[Friends] CHECK CONSTRAINT [FK_Friends_Users1]
GO
ALTER TABLE [dbo].[GroupMembers]  WITH CHECK ADD  CONSTRAINT [FK_GroupMembers_Groups] FOREIGN KEY([group_id])
REFERENCES [dbo].[Groups] ([group_id])
GO
ALTER TABLE [dbo].[GroupMembers] CHECK CONSTRAINT [FK_GroupMembers_Groups]
GO
ALTER TABLE [dbo].[GroupMembers]  WITH CHECK ADD  CONSTRAINT [FK_GroupMembers_Users] FOREIGN KEY([user_id])
REFERENCES [dbo].[Users] ([user_id])
GO
ALTER TABLE [dbo].[GroupMembers] CHECK CONSTRAINT [FK_GroupMembers_Users]
GO
ALTER TABLE [dbo].[Groups]  WITH CHECK ADD  CONSTRAINT [FK_Groups_Users] FOREIGN KEY([created_by])
REFERENCES [dbo].[Users] ([user_id])
GO
ALTER TABLE [dbo].[Groups] CHECK CONSTRAINT [FK_Groups_Users]
GO
ALTER TABLE [dbo].[LoginHistory]  WITH CHECK ADD  CONSTRAINT [FK_LoginHistory_Users] FOREIGN KEY([user_id])
REFERENCES [dbo].[Users] ([user_id])
GO
ALTER TABLE [dbo].[LoginHistory] CHECK CONSTRAINT [FK_LoginHistory_Users]
GO
ALTER TABLE [dbo].[Messages]  WITH CHECK ADD  CONSTRAINT [FK_Messages_Users] FOREIGN KEY([sender_id])
REFERENCES [dbo].[Users] ([user_id])
GO
ALTER TABLE [dbo].[Messages] CHECK CONSTRAINT [FK_Messages_Users]
GO
ALTER TABLE [dbo].[Messages]  WITH CHECK ADD  CONSTRAINT [FK_Messages_Users1] FOREIGN KEY([receiver_id])
REFERENCES [dbo].[Users] ([user_id])
GO
ALTER TABLE [dbo].[Messages] CHECK CONSTRAINT [FK_Messages_Users1]
GO
ALTER TABLE [dbo].[SpamReports]  WITH CHECK ADD  CONSTRAINT [FK_SpamReports_Messages] FOREIGN KEY([message_id])
REFERENCES [dbo].[Messages] ([message_id])
GO
ALTER TABLE [dbo].[SpamReports] CHECK CONSTRAINT [FK_SpamReports_Messages]
GO
ALTER TABLE [dbo].[SpamReports]  WITH CHECK ADD  CONSTRAINT [FK_SpamReports_Users] FOREIGN KEY([reported_by])
REFERENCES [dbo].[Users] ([user_id])
GO
ALTER TABLE [dbo].[SpamReports] CHECK CONSTRAINT [FK_SpamReports_Users]
GO
ALTER TABLE [dbo].[SpamReports]  WITH CHECK ADD  CONSTRAINT [FK_SpamReports_Users1] FOREIGN KEY([reported_user])
REFERENCES [dbo].[Users] ([user_id])
GO
ALTER TABLE [dbo].[SpamReports] CHECK CONSTRAINT [FK_SpamReports_Users1]
GO
USE [master]
GO
ALTER DATABASE [Users] SET  READ_WRITE 
GO
