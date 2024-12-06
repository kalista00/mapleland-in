import React from "react";
import { List, ListItem, ListItemText } from "@mui/material";

const PeopleList = ({ roomId }) => {
  // 샘플 데이터
  const people = [
    { id: 1, name: "사용자1" },
    { id: 2, name: "사용자2" },
    { id: 3, name: "사용자3" },
  ];

  return (
    <List>
      {people.map((person) => (
        <ListItem key={person.id}>
          <ListItemText primary={person.name} />
        </ListItem>
      ))}
    </List>
  );
};

export default PeopleList;
