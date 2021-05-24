import "./ScheduledList.css"
import { List, Avatar, Space, Tag, Radio, Button, message } from 'antd';
import {StarOutlined, ClockCircleOutlined, CheckCircleTwoTone,CalendarTwoTone } from '@ant-design/icons';
import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from 'react-redux';
import { CastVote, FetchAllActivePolls, FetchScheduledPolls } from '../../store/actions/Action';
import RandomColor from '../../constants/RandomColor';
import moment from 'moment';
import LoadingPolls from '../loading-polls-content/LoadingPolls';
import ProgressBar from "@ramonak/react-progress-bar";
import AddToCalendar from 'react-add-to-calendar';


const IconText = ({ icon, text }) => (
    <Space>
      {React.createElement(icon)}
      {text}
    </Space>
  );
  
function ScheduledList(){
    const[value,setValue]=useState("")
    const dispatch=useDispatch();
    const ScheduledPolls=useSelector(state=>state.poll)
    const auth=useSelector(state=>state.auth)
    const error=useSelector(state=>state.error)
    
    useEffect(() => {
      dispatch(FetchScheduledPolls())
      
     },[])
  
     
      
  const onChange = e => {
    console.log('radio checked', e.target.value);
    setValue(e.target.value)  
  };
  
  const setReminder=(items)=>{
  window.location.href=`https://calendar.google.com/calendar/u/0/r/eventedit?dates=${moment(items.scheduledTime).format('YYYYMMDD[T]HHmmss[Z]')}/${moment(items.closingTime).format('YYYYMMDD[T]HHmmss[Z]')}&location=Nairobi,+Kenya&text=Poll-Schedule+Event&details=${items.question}`
  }
  
 
  const RenderVoteButton=({pid})=>{
   return(
      <Button key={pid} disabled={true} className="vote-button" type="primary" shape="round" size="default">Vote</Button> 
      );
    
    }
  
  const RenderOptionsOrResults=({options,pollId})=>{
       return(
        <div className="options">
        <Radio.Group  onChange={onChange}>
        <Space direction="vertical">
          {options.map((choices,i)=>{
            return <Radio key={i} disabled={true} value={choices.id}>{choices.option}</Radio>
     
          })}
     
      </Space>
        </Radio.Group>
      <div>
    
      </div>
        </div>
       )
    }
  const RenderIfFetched=()=>{
  
    if(ScheduledPolls.hasFetchedScheduledPolls===true && error.hasFetchedScheduledErrors===false){
  
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
        dataSource={ScheduledPolls.scheduledPollData}
        footer={
          <div>
            <b>@Polling App</b> 2021
          </div>
        }
        renderItem={item => (
          <List.Item
            key={item.id}
            actions={[
              <IconText icon={CalendarTwoTone } text={  <Button ghost size="small"
              onClick={()=>{setReminder(item)}}
              >
              Set Reminder
            </Button>} key="list-vertical-message" />,
              <RenderVoteButton pid={item.id}/>
            ]}
           
          >
            <List.Item.Meta
              avatar={item.createdBy.imageurl==="none"?  <Avatar style={{backgroundColor:RandomColor()}} >
                  {item.createdBy.username.charAt(0).toUpperCase()}
              </Avatar>:<Avatar src={item.createdBy.imageurl} />}
              title={<b>{item.createdBy.username}</b>}
              description={<Tag icon={<ClockCircleOutlined />} color="processing">
                {"Opens "+moment(item.scheduledTime).fromNow()}
            </Tag>}
            />
            {<div className="poll">
              <div className="question">
                {item.question}
              </div>
              <RenderOptionsOrResults options={item.options} pollId={item.id}/>
              </div>
              
              }
          </List.Item>
        )}
      />
    )
    }
    return(
      <LoadingPolls/>
    )
  }
  return(
      <RenderIfFetched/>
  );

}
export default ScheduledList;