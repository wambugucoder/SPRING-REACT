import { List, Avatar, Space, Tag, Radio, Input, Button } from 'antd';
import {StarOutlined, ClockCircleOutlined } from '@ant-design/icons';
import React, { useCallback, useEffect, useMemo, useState } from "react";
import { useDispatch, useSelector } from 'react-redux';
import { FetchAllActivePolls } from '../../store/actions/Action';
import RandomColor from '../../constants/RandomColor';
import moment from 'moment';
import "./ActivePoll.css";



const IconText = ({ icon, text }) => (
  <Space>
    {React.createElement(icon)}
    {text}
  </Space>
);


function ActivePoll(){
  const[value,setValue]=useState("")
  const dispatch=useDispatch();
  const ActivePolls=useSelector(state=>state.poll)
  const error=useSelector(state=>state.error)


  useEffect(() => {
    dispatch(FetchAllActivePolls())
   
   },[])

 

const onChange = e => {
  console.log('radio checked', e.target.value);
  setValue(e.target.value)
  
};
const RenderIfFetched=()=>{

  if(ActivePolls.hasFetchedAllActivePolls===true && error.hasActivationErrors===false){

    return(
      <List
      itemLayout="vertical"
      size="large"
      pagination={{
        onChange: page => {
          console.log(page);
        },
        pageSize: 10,
      }}
      dataSource={ActivePolls.pollsData}
      footer={
        <div>
          <b>ant design</b> footer part
        </div>
      }
      renderItem={item => (
        <List.Item
          key={item.id}
          actions={[
            <IconText icon={StarOutlined} text={item.votes.length+" "+"votes"} key="list-vertical-star-o" />,
            <IconText icon={ClockCircleOutlined} text={"posted "+moment(item.createdAt).fromNow()} key="list-vertical-message" />,
            <div> <Button className="vote-button" type="primary" shape="round" size="default">Vote</Button></div> 
          ]}
         
        >
          <List.Item.Meta
            avatar={item.createdBy.imageurl==="none"?  <Avatar style={{backgroundColor:RandomColor()}} >
                {item.createdBy.username.charAt(0).toUpperCase()}
            </Avatar>:<Avatar src={item.createdBy.imageurl} />}
            title={<b>{item.createdBy.username}</b>}
            description={<Tag icon={<ClockCircleOutlined />} color="processing">
              {moment(item.closingTime).diff(moment(),"hours")>1?"Closes in "+ moment(item.closingTime).diff(moment(),"hours")+" hour(s)":
              "Closes in "+ moment(item.closingTime).diff(moment(),"minutes")+" minute(s)"
              }
          </Tag>}
          />
          {<div className="poll">
            <div className="question">
              {item.question}
            </div>
            <div className="options">
            <Radio.Group  onChange={onChange}>
            <Space direction="vertical">
              {item.options.map((choices,i)=>{
           return <Radio key={i} value={choices.id}>{choices.option}</Radio>
              })}
         
          </Space>
            </Radio.Group>

            </div>
            </div>}
        </List.Item>
      )}
    />
  )
  }
  return(
    <div>Loading...</div>
  )
}
return(
    <RenderIfFetched/>
);
}
export default ActivePoll;