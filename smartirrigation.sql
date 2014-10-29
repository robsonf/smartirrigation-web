-- phpMyAdmin SQL Dump
-- version 3.4.5
-- http://www.phpmyadmin.net
--
-- Servidor: localhost
-- Tempo de Geração: 29/10/2014 às 17h03min
-- Versão do Servidor: 5.5.16
-- Versão do PHP: 5.3.8

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Banco de Dados: `smartirrigation`
--

-- --------------------------------------------------------

--
-- Estrutura da tabela `agenda`
--

CREATE TABLE IF NOT EXISTS `agenda` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `estado` tinyint(1) DEFAULT NULL,
  `seg` tinyint(1) DEFAULT NULL,
  `ter` tinyint(1) DEFAULT NULL,
  `qua` tinyint(1) DEFAULT NULL,
  `qui` tinyint(1) DEFAULT NULL,
  `sex` tinyint(1) DEFAULT NULL,
  `sab` tinyint(1) DEFAULT NULL,
  `dom` tinyint(1) DEFAULT NULL,
  `horaInicial` varchar(5) NOT NULL DEFAULT '00:00',
  `horaFinal` varchar(5) NOT NULL DEFAULT '00:00',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=6 ;

--
-- Extraindo dados da tabela `agenda`
--

INSERT INTO `agenda` (`id`, `estado`, `seg`, `ter`, `qua`, `qui`, `sex`, `sab`, `dom`, `horaInicial`, `horaFinal`) VALUES
(1, 1, 1, 0, 0, 0, 0, 0, 0, '00:00', '01:00'),
(2, 0, 0, 0, 1, 0, 0, 0, 0, '10:00', '22:00');

-- --------------------------------------------------------

--
-- Estrutura da tabela `agenda_setores`
--

CREATE TABLE IF NOT EXISTS `agenda_setores` (
  `agenda_id` int(11) NOT NULL,
  `setor_id` int(11) NOT NULL,
  PRIMARY KEY (`agenda_id`,`setor_id`),
  KEY `fk_agenda_has_setor_setor1` (`setor_id`),
  KEY `fk_agenda_has_setor_agenda` (`agenda_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Extraindo dados da tabela `agenda_setores`
--

INSERT INTO `agenda_setores` (`agenda_id`, `setor_id`) VALUES
(1, 2),
(2, 3);

-- --------------------------------------------------------

--
-- Estrutura da tabela `setor`
--

CREATE TABLE IF NOT EXISTS `setor` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `descricao` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `descricao_UNIQUE` (`descricao`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=5 ;

--
-- Extraindo dados da tabela `setor`
--

INSERT INTO `setor` (`id`, `descricao`) VALUES
(1, 'setor1'),
(2, 'setor2'),
(3, 'setor3'),
(4, 'setor4');

--
-- Restrições para as tabelas dumpadas
--

--
-- Restrições para a tabela `agenda_setores`
--
ALTER TABLE `agenda_setores`
  ADD CONSTRAINT `fk_agenda_has_setor_agenda` FOREIGN KEY (`agenda_id`) REFERENCES `agenda` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_agenda_has_setor_setor1` FOREIGN KEY (`setor_id`) REFERENCES `setor` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
