import { List, Avatar, Space, Tag ,Progress } from 'antd';
import {StarOutlined,CheckCircleOutlined} from '@ant-design/icons';
import React, { useEffect} from "react";
import { useDispatch, useSelector } from 'react-redux';
import { FetchClosedPolls } from '../../store/actions/Action';
import RandomColor from '../../constants/RandomColor';
import moment from 'moment';
import LoadingPolls from '../loading-polls-content/LoadingPolls';
import "../active-polls-component/ActivePoll.css"


var totalVotes=0
var optionVote=0

const IconText = ({ icon, text }) => (
  <Space>
    {React.createElement(icon)}
    {text}
  </Space>
);

function ClosedPollList(){
  const dispatch=useDispatch();
  const ClosedPolls=useSelector(state=>state.poll)
  const auth=useSelector(state=>state.auth)
  const error=useSelector(state=>state.error)
  
  useEffect(() => {
    //dispatch call
    dispatch(FetchClosedPolls())
    
   // eslint-disable-next-line react-hooks/exhaustive-deps
   },[])

    const CalculatePercentage=(pollId,choiceId)=>{
        var pollsCopy=[]
        var choices=[]
        var pollData=ClosedPolls.closedPolls.filter((item)=>{return item.id === pollId})
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
          return isNaN(results)?0:Math.round(results)
      }
      const DidUserVoteForThisChoice=(pollId,choiceId) =>{
        var pollsCopy=[]
        var pollData=ClosedPolls.closedPolls.filter((item)=>{return item.id === pollId})
        pollsCopy=pollData
        //Loop THROUGH POLL ARRAY TO FIND OPTIONS
        for(let eachPollData of pollsCopy){
          //Loop THROUGH EVERY OPTION
          for(let eachOption of eachPollData.options){
            //SEARCH IF USER VOTED FOR THIS CHOICE
              //IF CHOICE ID MATCHES THE CHOICEID
              if(eachOption.id===choiceId){
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
     
      const RenderOptionsOrResults=({options,pollId})=>{
          return(
           <div className="poll-results">
              {options.map((choices,i)=>{
                const choiceId=choices.id
             return <div className="results">
             <div>
             <span className="result-choice">{choices.option}</span>
             <span></span>
             <span>{ DidUserVoteForThisChoice(pollId,choiceId)?<CheckCircleOutlined style={{fontSize: 13}}/>:<span></span>}</span>
           
             </div>
            <span> 
            <Progress strokeWidth={14} percent={CalculatePercentage(pollId,choiceId)} status="active" /> 
            </span>
            <span></span>
            
     
             </div>
              })}
         </div>
          ) 
     }
   const RenderIfFetched=()=>{
   
     if(ClosedPolls.hasFetchedClosedPolls===true && error.hasFetchedClosedPollsErrors===false){
   
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
         dataSource={ClosedPolls.closedPolls}
         renderItem={item => (
           <List.Item
             key={item.id}
             actions={[
               
               <IconText icon={StarOutlined} text={item.votes.length +" votes"}/>,
                
             ]}
            
           >
             <List.Item.Meta
                avatar={<Avatar src="https://netstorage-tuko.akamaized.net/images/5cd3d44ba68de436.png?&imwidth=800" />}
                title={<b>JKUAT ELECTION</b>}
               description={<Tag  color="red">
                 {"Closed  "+ moment(item.closingTime).fromNow()}
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
export default ClosedPollList;