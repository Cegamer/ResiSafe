-- MySQL dump 10.13  Distrib 8.0.32, for Win64 (x86_64)
--
-- Host: localhost    Database: app
-- ------------------------------------------------------
-- Server version	8.0.32

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `registro_visitantes`
--

DROP TABLE IF EXISTS `registro_visitantes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `registro_visitantes` (
  `ID_REGISTRO` int NOT NULL AUTO_INCREMENT,
  `ID_VISITANTE` int NOT NULL,
  `ID_RESIDENTE_VINCULADO` int NOT NULL,
  `ID_VIGILANTE_QUE_REGISTRA` int NOT NULL,
  `FECHA` date NOT NULL,
  `HORA_INGRESO` time NOT NULL,
  `HORA_SALIDA` time DEFAULT NULL,
  PRIMARY KEY (`ID_REGISTRO`),
  KEY `registro_visitantes` (`ID_VISITANTE`),
  KEY `registro_visitantes_ibfk_2` (`ID_RESIDENTE_VINCULADO`),
  KEY `registro_visitantes_ibfk_3` (`ID_VIGILANTE_QUE_REGISTRA`),
  CONSTRAINT `registro_visitantes` FOREIGN KEY (`ID_VISITANTE`) REFERENCES `visitante` (`ID_VISITANTE`),
  CONSTRAINT `registro_visitantes_ibfk_2` FOREIGN KEY (`ID_RESIDENTE_VINCULADO`) REFERENCES `perfil` (`ID_Perfil`),
  CONSTRAINT `registro_visitantes_ibfk_3` FOREIGN KEY (`ID_VIGILANTE_QUE_REGISTRA`) REFERENCES `perfil` (`ID_Perfil`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `registro_visitantes`
--

LOCK TABLES `registro_visitantes` WRITE;
/*!40000 ALTER TABLE `registro_visitantes` DISABLE KEYS */;
/*!40000 ALTER TABLE `registro_visitantes` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-05-01 14:29:37
