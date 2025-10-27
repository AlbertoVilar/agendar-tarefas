-- data.sql: seed de usuário usando função Java BCryptUtil via H2 ALIAS
CREATE ALIAS IF NOT EXISTS BCRYPT FOR "com.vilardev.Daily.util.BCryptUtil.encode";

INSERT INTO usuario (nome, email, senha)
SELECT 'Alberto Vilar', 'albertovilar1@gmail.com', BCRYPT('132747') WHERE NOT EXISTS (SELECT 1 FROM usuario WHERE email = 'albertovilar1@gmail.com');