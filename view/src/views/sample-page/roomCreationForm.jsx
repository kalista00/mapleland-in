import { Button, Grid, Slider, TextField, Typography } from "@mui/material"; // Typography 추가
import { useState } from "react";
import { apiRequest } from "utils/api-request"; // apiRequest 임포트

const RoomCreationForm = ({ onClose }) => {
  const [name, setName] = useState("");
  const [continent, setContinent] = useState(""); // 상태 변수 추가
  const [capacity, setCapacity] = useState(2);
  const [levelRange, setLevelRange] = useState([1, 200]); // 초기 값 설정

  const handleSubmit = async (e) => {
    e.preventDefault();
    const newRoom = {
      name: name,
      continent: continent,
      capacity: capacity,
      levelRange: levelRange,
    };
    const url = import.meta.env.VITE_APP_API_URL;
    try {
      await apiRequest.postData(`${url}/api/room`, newRoom);
      onClose();
    } catch (error) {
      console.error("방 생성 실패:", error);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <Grid container direction="column" spacing={2}>
        <Grid item>
          <TextField
            label="파티 이름"
            value={name}
            onChange={(e) => setName(e.target.value)}
            fullWidth
            required
          />
        </Grid>

        {/* 맵 선택 폼 */}
        <Grid item>
          <TextField
            select
            label="대륙 선택"
            value={continent}
            onChange={(e) => setContinent(e.target.value)}
            fullWidth
            required
            SelectProps={{ native: true }}
          >
            <option value=""></option>
            <option value="VICT">빅토리아 아일랜드</option>
            <option value="ELNA">엘나스</option>
            <option value="LUDI">루디브리엄</option>
            <option value="LEAF">리프레</option>
          </TextField>
        </Grid>

        <Grid item>
          <TextField
            label="최대 인원"
            type="number"
            value={capacity}
            onChange={(e) => setCapacity(Number(e.target.value))} // 숫자 변환 추가
            fullWidth
            InputProps={{
              inputProps: { min: 2, max: 30 },
            }}
            required
          />
        </Grid>

        <Grid item>
          <Typography gutterBottom>
            레벨 범위 설정 (현재: {levelRange[0]} - {levelRange[1]})
          </Typography>
          <Slider
            value={levelRange}
            onChange={(e, newValue) => setLevelRange(newValue)}
            valueLabelDisplay="auto"
            min={1}
            max={200}
          />
        </Grid>

        <Grid item container justifyContent="flex-end" spacing={1}>
          <Grid item>
            <Button variant="outlined" color="secondary" onClick={onClose}>
              취소
            </Button>
          </Grid>
          <Grid item>
            <Button variant="contained" color="secondary" type="submit">
              생성
            </Button>
          </Grid>
        </Grid>
      </Grid>
    </form>
  );
};

export default RoomCreationForm;
