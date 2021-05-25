import {  ACTIVE_POLLS, CAST_VOTE, CLEANUP_POLL,CLOSED_POLLS,CREATE_NON_SCHEDULED_POLL, CREATE_SCHEDULED_POLL, IS_LOADING, SCHEDULED_POLLS } from "../actions/actionTypes";

const INITIAL_STATE = {
    isLoading:false,
    hasCreatedNonScheduledPoll:false,
    hasCreatedScheduledPoll:false,
    hasFetchedAllActivePolls:false,
    hasCastedVote:false,
    hasFetchedScheduledPolls:false,
    hasFetchedClosedPolls:false,
    closedPolls:[],
    scheduledPollData:[],
    pollsData:[],

};
 
// eslint-disable-next-line import/no-anonymous-default-export
export default (state = INITIAL_STATE, action) => {
    switch (action.type) {
        case IS_LOADING:
            return {...state,
            isLoading:true,
            }
            case CREATE_NON_SCHEDULED_POLL:
                return {...state,
                isLoading:false,
                hasCreatedNonScheduledPoll:true
                }
                case CREATE_SCHEDULED_POLL:
                    return {...state,
                    isLoading:false,
                    hasCreatedScheduledPoll:true
                    }
                    case ACTIVE_POLLS:
                        return {...state,
                        isLoading:false,
                        hasFetchedAllActivePolls:true,
                        pollsData:action.payload
                        }  
                    case SCHEDULED_POLLS:
                        return{...state,
                        isLoading:false,
                        hasFetchedScheduledPolls:true,
                        scheduledPollData:action.payload
                    
                     }
                    case CAST_VOTE:
                        return{...state,
                            isLoading:false,
                            hasCastedVote:true
                        }
                        case CLOSED_POLLS:
                        return{...state,
                            isLoading:false,
                            hasFetchedClosedPolls:true,
                            closedPolls:action.payload
                        }
                        case CLEANUP_POLL:
                            return {...state,
                            isLoading:false,
                            hasCreatedNonScheduledPoll:false,
                            hasCreatedScheduledPoll:false,
                            hasCastedVote:false,
                            }      
               
                   

                    
        default:
            return state
    }
}