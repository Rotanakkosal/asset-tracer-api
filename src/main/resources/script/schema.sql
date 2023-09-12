-- UUID generate v4
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
select gen_random_uuid();

-------------------------------------------------------------------
-- Command for create role table
create table role (
    id uuid not null default gen_random_uuid() primary key ,
    name varchar(100) not null
);

-- Command for add data to role table
insert into role (name) VALUES ('ADMIN'),
                               ('USER');

-------------------------------------------------------------------
-- Command for create user account table
create table user_acc (
      id uuid not null default gen_random_uuid() primary key ,
      name varchar(100) ,
      gender varchar(10) ,
      email varchar(100) not null ,
      password text not null ,
      phone varchar(50) ,
      address text ,
      image text ,
      is_enabled boolean default false ,
      is_account_non_locked boolean default false ,
      is_account_non_expired boolean default false,
      created_at timestamp default current_timestamp ,
      updated_at timestamp,
      deleted_at timestamp
);

-------------------------------------------------------------------
-- Command for organization
create table organization (
    id uuid not null default gen_random_uuid() primary key ,
    name varchar(100) not null ,
    code varchar(50) not null ,
    address text ,
    logo text ,
    created_at timestamp default current_timestamp ,
    created_by uuid references user_acc(id) not null ,
    updated_at timestamp ,
    updated_by uuid ,
    deleted_at timestamp,
    deleted_by uuid
);

-------------------------------------------------------------------
-- Command for create email verification table
create table email_verification (
    id uuid not null default gen_random_uuid() primary key ,
    user_id uuid references user_acc(id) not null ,
    code text not null ,
    is_verified boolean default false ,
    expire_at date ,
    created_at timestamp default current_timestamp
);

-- Command for create user reset password
create table user_reset_password (
     id uuid not null default gen_random_uuid() primary key ,
     user_id uuid references user_acc(id) not null ,
     code text not null ,
     expire_at date ,
     created_at timestamp default current_timestamp
);

-------------------------------------------------------------------
-- sql command for create suer category table
create table super_category (
    id uuid not null default gen_random_uuid() primary key ,
    name varchar(100) not null ,
    icon text ,
    organization_id uuid references organization(id) not null ,
    created_at timestamp default current_timestamp ,
    created_by uuid references user_acc(id) not null ,
    updated_at timestamp ,
    updated_by uuid references user_acc(id) ,
    deleted_at timestamp ,
    deleted_by  uuid references user_acc(id)
);

-------------------------------------------------------------------
-- Command for create suer category table
create table normal_category (
    id uuid not null default gen_random_uuid() primary key ,
    name varchar(100) not null ,
    icon text ,
    super_category_id uuid references super_category(id) not null ,
    organization_id uuid references organization(id) not null ,
    created_at timestamp default current_timestamp ,
    created_by uuid references user_acc(id) ,
    updated_at timestamp ,
    updated_by uuid references user_acc(id) ,
    deleted_at timestamp ,
    deleted_by  uuid references user_acc(id)
);

-------------------------------------------------------------------
-- Command for create room table
create table room (
    id uuid not null default gen_random_uuid() primary key ,
    name varchar(100) not null ,
    type varchar(50) ,
    floor varchar(50) ,
    description text ,
    image text ,
    organization_id uuid references organization(id) not null ,
    created_at timestamp default current_timestamp ,
    created_by uuid references user_acc(id) not null ,
    updated_at timestamp ,
    updated_by uuid references user_acc(id) ,
    deleted_at timestamp ,
    deleted_by  uuid references user_acc(id)
);

-------------------------------------------------------------------
-- Command for create invoice table
create table invoice (
    id uuid not null default gen_random_uuid() primary key ,
    invoice_code varchar(50) not null ,
    purchase_by varchar(50) not null ,
    supplier varchar(100) not null ,
    purchase_date date not null ,
    image text ,
    organization_id uuid references organization(id) not null ,
    created_at timestamp default current_timestamp ,
    created_by uuid references user_acc(id) not null ,
    updated_at timestamp ,
    updated_by uuid references user_acc(id),
    deleted_at timestamp ,
    deleted_by  uuid references user_acc(id)
);

-------------------------------------------------------------------
-- Command for create item detail
create table item_detail (
    id uuid not null default gen_random_uuid() primary key ,
    invoice_id uuid references invoice(id) ,
    name varchar(100) not null ,
    model varchar(100) ,
    image text ,
    qty int2 not null ,
    unit_price numeric(8, 2) ,
    discount int default 0 ,
    description text,
    organization_id uuid references organization(id) not null ,
    category_id uuid references normal_category(id) not null ,
    created_at timestamp default current_timestamp ,
    created_by uuid references user_acc(id) not null ,
    updated_at timestamp ,
    updated_by uuid references user_acc(id),
    deleted_at timestamp ,
    deleted_by  uuid references user_acc(id)
);

-------------------------------------------------------------------
-- Command for create asset table
create table asset (
    id uuid not null default gen_random_uuid() primary key ,
    item_detail_id uuid references item_detail(id) not null ,
    type varchar(100) ,
    image varchar(100) ,
    label_code varchar(100) ,
    serial_code varchar(100) ,
    status varchar(100) not null ,
    owner varchar(100) ,
    description text,
    room_id uuid references room(id) ,
    organization_id uuid references organization(id) not null ,
    created_at timestamp default current_timestamp ,
    created_by uuid references user_acc(id) not null ,
    updated_at timestamp ,
    updated_by uuid references user_acc(id) ,
    deleted_at timestamp ,
    deleted_by  uuid references user_acc(id)
);

-------------------------------------------------------------------
-- Command for create organization detail
create table organization_detail (
     user_id uuid references user_acc(id) not null ,
     organization_id uuid references  organization(id) not null ,
     role_id uuid references role(id) not null ,
     is_active boolean default false ,
     is_member boolean default false ,
     status varchar(50) ,
     created_at timestamp default current_timestamp ,
     created_by uuid references user_acc(id) not null ,
     updated_at timestamp ,
     updated_by uuid references user_acc(id),
     deleted_at timestamp ,
     deleted_by uuid references user_acc(id) ,
     primary key (user_id, organization_id)
);