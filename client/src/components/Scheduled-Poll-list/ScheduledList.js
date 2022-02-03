import "./ScheduledList.css"
import { List, Avatar, Space, Tag, Radio, Button } from 'antd';
import React, { useEffect } from "react";
import { useDispatch, useSelector } from 'react-redux';
import {FetchScheduledPolls } from '../../store/actions/Action';
import RandomColor from '../../constants/RandomColor';
import moment from 'moment';
import LoadingPolls from '../loading-polls-content/LoadingPolls';



  
function ScheduledList(){

    const dispatch=useDispatch();
    const ScheduledPolls=useSelector(state=>state.poll)
    const error=useSelector(state=>state.error)
    
    useEffect(() => {
      dispatch(FetchScheduledPolls())
      
     // eslint-disable-next-line react-hooks/exhaustive-deps
     },[])
  
     
      
  const onChange = e => {
    console.log('radio checked', e.target.value);
     
  };
  
  const setReminder=(items)=>{
  window.location.href=`https://calendar.google.com/calendar/u/0/r/eventedit?dates=${moment(items.scheduledTime).format('YYYYMMDD[T]HHmmss')}/${moment(items.closingTime).format('YYYYMMDD[T]HHmmss')}&location=Nairobi,+Kenya&text=Poll-Schedule+Event&details=${items.question}`
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
        renderItem={item => (
          <List.Item
            key={item.id}
            actions={[
                <Button size="small"
                type="primary"
                 key="list-vertical-message"
                onClick={()=>{setReminder(item)}}
              >
              Set Reminder
            </Button> ,
              <RenderVoteButton pid={item.id}/>
            ]}
           
          >
            <List.Item.Meta
               avatar={<Avatar src="https://netstorage-tuko.akamaized.net/images/5cd3d44ba68de436.png?&imwidth=800" />}
               title={<b>JKUAT ELECTION</b>}
              description={<Tag color="cyan">
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