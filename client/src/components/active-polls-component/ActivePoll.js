import { List, Avatar, Space, Tag, Radio, Button, message } from 'antd';
import {StarOutlined, ClockCircleOutlined, CheckCircleTwoTone } from '@ant-design/icons';
import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from 'react-redux';
import { CastVote, FetchAllActivePolls } from '../../store/actions/Action';
import RandomColor from '../../constants/RandomColor';
import moment from 'moment';
import "./ActivePoll.css";
import LoadingPolls from '../loading-polls-content/LoadingPolls';
import ProgressBar from "@ramonak/react-progress-bar";


var totalVotes=0
var optionVote=0

const IconText = ({ icon, text }) => (
  <Space>
    {React.createElement(icon)}
    {text}
  </Space>
);


function ActivePoll(props){
  const[value,setValue]=useState("")
  const [disabled,setDisabled]=useState([])
  const dispatch=useDispatch();
  const ActivePolls=useSelector(state=>state.poll)
  const auth=useSelector(state=>state.auth)
  const error=useSelector(state=>state.error)
  
  useEffect(() => {
    dispatch(FetchAllActivePolls())
    
   },[])

   
  

const HasUserVoted=(pollId)=>{

  var pollsCopy=[]
//LOOP THROUGH EACH POLL
//SEARCH FOR VOTES
//CHECK FOR USE IN VOTES VIA ID
var pollData=ActivePolls.pollsData.filter((item)=>{return item.id === pollId})
pollsCopy=pollData
for(let eachPoll of pollsCopy){
  for(let eachVote of eachPoll.votes){
  if( eachVote.user.id===auth.user.Id){
    return true;
  }
  }
  return false;
}
} 
 const CalculatePercentage=(pollId,choiceId)=>{
  var pollsCopy=[]
  var choices=[]
  var pollData=ActivePolls.pollsData.filter((item)=>{return item.id === pollId})
  pollsCopy=pollData
  for(let eachPoll of pollsCopy){
    totalVotes=eachPoll.votes.length
    for(let anyOption of eachPoll.options){
     choices.push(anyOption)
    }}
    for(let choice of choices){
      if(choice.id===choiceId){
         optionVote=choice.incomingvotes.length
      }
    }
    var results=(optionVote/totalVotes)*(100) 
    return results
}
const DidUserVoteForThisCoice=(pollId,choiceId) =>{
  var pollsCopy=[]
  var pollData=ActivePolls.pollsData.filter((item)=>{return item.id === pollId})
  pollsCopy=pollData
  //LOOP THROUGH POLL ARRAY TO FIND OPTIONS
  for(let eachPollData of pollsCopy){
    //LOOP THROUGH EVERY OPTION
    for(let eachOption of eachPollData.options){
      //SEARCH IF USER VOTED FOR THIS CHOICE
        //IF CHOICE ID MATCHES THE CHOICEID
        if(eachOption.id===choiceId){
          console.log("Tuko hapa 4")
          for(let incomingVotes of eachOption.incomingvotes){
           if(incomingVotes.user.id===auth.user.Id){
              return true;
            }
            else{
              continue
            }  
        }
      }
    }
    return false;
  
  }
  
}
const DoesPollContainCorrectChoice=(pollId,choiceId)=>{
  var pollsCopy=[]
  var choices=[]
  var pollData=ActivePolls.pollsData.filter((item)=>{return item.id === pollId})
  pollsCopy=pollData

  for(let eachPoll of pollsCopy){
    for(let anyOption of eachPoll.options){
     // console.log(anyOption)
     choices.push(anyOption)
    }}
    console.log(choices)
    for(let choice of choices){
      if(choice.id===choiceId){
        return true;
      }
     
    }
    return false;
}

const onChange = e => {
  console.log('radio checked', e.target.value);
  setValue(e.target.value)
  
};

const CastYourVote=(pid)=>{

  if(value===""){
   return message.error('Choose An Option ');
  }
  if(DoesPollContainCorrectChoice(pid,value)===false){
   return  message.error('Choose An Option In the Specific Poll');
  }
  const uid=auth.user.Id
  const cid=value
  setDisabled([...disabled,pid])
  dispatch(CastVote(uid,pid,cid))
  
  
}
const RenderVoteButton=({pid})=>{
  if(HasUserVoted(pid)){
    return(
     <div key={pid}></div>
      );

  }
  return(
    <Button key={pid} disabled={disabled.indexOf(pid)!==-1} className="vote-button" type="primary" shape="round" size="default"  onClick={()=>{CastYourVote(pid)}}>Vote</Button> 
    );
  
  }

const RenderOptionsOrResults=({options,pollId})=>{
     if(HasUserVoted(pollId)){
       return(
        <div className="poll-results">
           {options.map((choices,i)=>{
             const choiceId=choices.id
          return <div className="results">
            <span className="result-choice">{choices.option}</span>
            <span> </span>
            <span>{ DidUserVoteForThisCoice(pollId,choiceId)?<CheckCircleTwoTone/>:<span></span>}</span>
             <span className="percent"> <ProgressBar className="pg-chart"
            completed={CalculatePercentage(pollId,choiceId)}
            bgColor="#3C6177"
            height="35px"
            width="75%"
            borderRadius="7px"
            labelAlignment="left"
            baseBgColor="#000000"
            labelColor="#fffff"
    /></span>
           
    
            </div>
             })}
        </div>
       ) 
     }
     return(
      <div className="options">
      <Radio.Group  onChange={onChange}>
      <Space direction="vertical">
        {options.map((choices,i)=>{
          return <Radio key={i} disabled={disabled.indexOf(pollId)!==-1} value={choices.id}>{choices.option}</Radio>
   
        })}
   
    </Space>
      </Radio.Group>
    <div>
  
    </div>
      </div>
     )
  }
const RenderIfFetched=()=>{

  if(ActivePolls.hasFetchedAllActivePolls===true && error.hasActivePollErrors===false){

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
          <b>@Polling App</b> 2021
        </div>
      }
      renderItem={item => (
        <List.Item
          key={item.id}
          actions={[
            <IconText icon={StarOutlined} text={item.votes.length+" "+"votes"} key="list-vertical-star-o" />,
            <IconText icon={ClockCircleOutlined} text={"posted "+moment(item.createdAt).fromNow()} key="list-vertical-message" />,
            <RenderVoteButton pid={item.id}/>
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
export default ActivePoll;