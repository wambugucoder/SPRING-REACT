import { Skeleton, List, Avatar } from 'antd';
import { StarOutlined, LikeOutlined, MessageOutlined } from '@ant-design/icons';
import React from "react";

const listData = [];
for (let i = 0; i < 3; i++) {
  listData.push({
    href: 'https://ant.design',
    title: `ant design part ${i}`,
    avatar: 'https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png',
    description:
      'Ant Design, a design language for background applications, is refined by Ant UED Team.',
    content:
      'We supply a series of design principles, practical patterns and high quality design resources (Sketch and Axure), to help people create their product prototypes beautifully and efficiently.',
  });
}

const IconText = ({ icon, text }) => (
    <span>
      {React.createElement(icon, { style: { marginRight: 8 } })}
      {text}
    </span>
  );

function LoadingPolls(){
    return (
        <>
          
        <List
            itemLayout="vertical"
            size="large"
            dataSource={listData}
            renderItem={item => (
              <List.Item
                key={item.title}
                
              >
                <Skeleton loading={true} active avatar>
                  <List.Item.Meta
                    avatar={<Avatar src={item.avatar} />}
                    title={<a href={item.href}>{item.title}</a>}
                    description={item.description}
                  />
                  {item.content}
                </Skeleton>
              </List.Item>
            )}
          />
        </>
      );

}
export default LoadingPolls;