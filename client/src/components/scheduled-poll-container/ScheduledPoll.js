 import { Form, Input, Button,DatePicker,message, Space} from 'antd';
import { QuestionOutlined, PlusOutlined, MinusCircleOutlined } from '@ant-design/icons';
import "./ScheduledPoll.css";
import moment from "moment";

const { RangePicker } = DatePicker;
const onFinish = (values) => {

    

  /*const options=[];
   for (const singleOption of values.options) {
        options.push({"option":singleOption})
    }
    const data={
        question:values.question,
        closingTime:moment(values.closingTime._d).format(),
        options:options
    }
    */
   console.log(values)
   
    //dispatch(LoginUser(UserData));
  }

  function DisabledDate(current) {
    // Can not select days before today and today
    return current && current < moment().startOf('day');
  }
 

function ScheduledPoll(){
const ErrorMessage = () => {
    message.error('Maximum Number of Choices Reached');
    };
    
const NonScheduledForm=()=>{
    return(
        <Form
        name="dynamic_form_item" 
        initialValues={{ remember: true }}
        onFinish={onFinish}
      >
        <Form.Item
          name="question"
          rules={[{ required: true, message: "Question Cannot Be Empty"}]}
        >
          <Input prefix={<QuestionOutlined className="site-form-item-icon" />} placeholder="Question" />
        </Form.Item>
        <Form.List
        name="options"
        rules={[
          {
            validator: async (_, options) => {
              if (!options || options.length < 2) {
                return Promise.reject(new Error('At least 2 Options'));
              }   
            },
          },
        ]}
      >
        {(fields, { add,option, remove }, { errors }) => (
          <>
            {fields.map((field, index) => (
              <Form.Item
               required={false}
                key={field.key}
              >
                <Form.Item
                  {...field}
                  validateTrigger={['onChange', 'onBlur']}
                  rules={[
                    {
                      required: true,
                      whitespace: true,
                      message: "Please input an Option or delete this field.",
                    },
                  ]}
                  noStyle
                >
                  <Input placeholder="Option" style={{ width: '80%' }} />
                </Form.Item>
                {fields.length > 1 ? (
                  <MinusCircleOutlined
                    className="dynamic-delete-button"
                    onClick={() => remove(field.name)}
                  />
                ) : null}
              </Form.Item>
            ))}
            <Form.Item>
                 <Button
                 type="dashed"
                 onClick={() =>{fields.length < 5 ? add():ErrorMessage()} }
                 style={{ width: '60%' }}
                 icon={<PlusOutlined/>}
               >
                 Add Option
               </Button>
           <Form.ErrorList errors={errors} />
            </Form.Item>
          </>
        )}
      </Form.List>
      <Form.Item
          name="scheduledTime"
          rules={[
            {
              required: true,
              message: 'Scheduled Time is Required',
            },
            ({ getFieldValue }) => ({
                validator(_, value) {
                   
                  if (value && moment(value[0]._d).isBefore(moment.now())) {
                      return Promise.reject(new Error('You Cannot Enter A Past Date Or Time'));
                    
                  }
                  else if (value && moment(value[1]._d).diff(moment(value[0]._d),"minutes") < 30){
                      return Promise.reject(new Error('Adjust Your Closing Time So Poll Can Take More Than 30min'));
                   }
                   
                  return Promise.resolve();
                 
                },
              }),
          ]}
        >
         <RangePicker
         ranges={{
            Today: [moment().startOf("day"), moment().startOf("day")],
          }}
       disabledDate={DisabledDate}
       placeholder={["Opening Time","Closing Time"]}
       showTime={{
       hideDisabledOptions: true,
       defaultValue: [moment('00:00:00', 'HH:mm:ss'), moment('11:59:59', 'HH:mm:ss')],
      }}
      format="YYYY-MM-DD HH:mm:ss"
    />
        </Form.Item>
        <Form.Item>
          <Space/>
        </Form.Item>
            
        <Form.Item>
          <Button type="primary" htmlType="submit" className="login-form-button">
            Schedule Poll
          </Button>
        </Form.Item>


      </Form>
    );
}
return(
    <div className="non-scheduled-form">
<NonScheduledForm/>
    </div>

);

}
export default ScheduledPoll;