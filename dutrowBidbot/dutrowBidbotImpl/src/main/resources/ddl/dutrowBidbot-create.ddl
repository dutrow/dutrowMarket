
    create table DUTROW_BIDBOT_ACCT (
        userId varchar(255) not null,
        salesAccount varchar(255),
        salesPassword varchar(255),
        primary key (userId)
    );

    create table DUTROW_BIDBOT_ACCT_DUTROW_BIDBOT_ORDER (
        DUTROW_BIDBOT_ACCT_userId varchar(255) not null,
        orders_bidId bigint not null,
        unique (orders_bidId)
    );

    create table DUTROW_BIDBOT_ORDER (
        bidId bigint not null,
        auctionId bigint not null,
        complete boolean not null,
        finalBid float not null,
        maxBid float not null,
        result boolean not null,
        startBid float not null,
        bidder_userId varchar(255),
        primary key (bidId)
    );

    alter table DUTROW_BIDBOT_ACCT_DUTROW_BIDBOT_ORDER 
        add constraint FK8FC49222A00BA803 
        foreign key (orders_bidId) 
        references DUTROW_BIDBOT_ORDER;

    alter table DUTROW_BIDBOT_ACCT_DUTROW_BIDBOT_ORDER 
        add constraint FK8FC492223AE0FF29 
        foreign key (DUTROW_BIDBOT_ACCT_userId) 
        references DUTROW_BIDBOT_ACCT;

    alter table DUTROW_BIDBOT_ORDER 
        add constraint FK3CAB30A13C4E5EF5 
        foreign key (bidder_userId) 
        references DUTROW_BIDBOT_ACCT;
