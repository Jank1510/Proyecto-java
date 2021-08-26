CREATE SCHEMA `reto_5`;

CREATE TABLE reto_5.`usuario` (	
	`id` INT(11) NOT NULL, 
    `nombre` VARCHAR(16) NOT NULL,
    `apellido` VARCHAR(16) DEFAULT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE reto_5.`registros`(
	`registro` INT(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `operacion` INT NOT NULL,
    `usuario` INT NOT NULL,
    `resultado` VARCHAR(255) NOT NULL,
    `sistema` INT NOT NULL,
    `fecha` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,    
    KEY `K_usuario`(`usuario`),
    CONSTRAINT `fk_id_usuario_reg` FOREIGN KEY(`usuario`) REFERENCES `usuario`(`id`)ON UPDATE NO ACTION ON DELETE RESTRICT
);

CREATE TABLE reto_5.`entradas`(
	`id_valor` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`id_Registro` INT NOT NULL,
    `entradas` VARCHAR(255) NOT NULL,
    KEY `K_regiatro`(`id_Registro`),
    CONSTRAINT `fk_id_registro_entradas` FOREIGN KEY(`id_Registro`) REFERENCES `registros`(`registro`)ON UPDATE NO ACTION ON DELETE RESTRICT
);
