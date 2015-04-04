# DDLGenerator

--------config file in YAML.

config:
  database: db_6_216cb
  schema: dbo
table:
- name: department
  column:
  - name: id
    type: varchar(16)
    requried: true
    note: uuid for the department
  - name: name
    type: varchar(16)
    requried: true
    note: name for the department
  - name: type
    type: integer
    requried: true
    note: the type for the department
    valueEnum:
    - value: 1
      desc: real department
    - value: 2
      desc: virtual department
  constraint:
  - name: test_dept_pk
    type: PRIMARY KEY
    column:
    - id
  index:
  - name: test_dept_name_idx
    type: UNIQUE
    column:
    - name: name
      order: DESC

-------Generate Result
USE db_6_216cb;

IF EXISTS (SELECT * FROM SYS.TABLES WHERE NAME = 'department' AND TYPE = 'U'AND SCHEMA_NAME(SCHEMA_ID) = 'dbo')
  DROP TABLE dbo.department;
GO

CREATE TABLE dbo.department(
  id varchar(16)   -- uuid for the department
  ,name varchar(16)   -- name for the department
  ,type integer   -- the type for the department  [(1:real department) (2:virtual department) ]
  ,CONSTRAINT test_dept_pk PRIMARY KEY (id)
);

CREATE UNIQUE INDEX test_dept_name_idx ON dbo.department (
  name DESC
);