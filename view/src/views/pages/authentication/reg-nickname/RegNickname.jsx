import { Button, Grid, TextField, Typography } from "@mui/material";
import { useState } from "react";
import { apiRequest } from "utils/api-request";

const RegNickname = ({ onNicknameSet }) => {
  const [nickname, setNickname] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!nickname.trim()) {
      alert("닉네임을 입력해주세요.");
      return;
    }

    try {
      const apiUrl = import.meta.env.VITE_APP_API_URL;
      const url = import.meta.env.VITE_APP_URL;

      // 닉네임 설정 API 호출
      await apiRequest.patchData(`${apiUrl}/api/user/nickname`, { nickname });
      alert("닉네임이 설정되었습니다.");
      if (onNicknameSet) {
        onNicknameSet(nickname);
      }
      window.location.href = url;
    } catch (error) {
      console.error("닉네임 설정 실패:", error);
      alert("닉네임 설정에 실패하였습니다. 다시 시도해주세요.");
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <Grid container direction="column" spacing={2} alignItems="center">
        <Grid item xs={12} style={{ width: "100%" }}>
          <TextField
            label="ex) 홍길동"
            value={nickname}
            onChange={(e) => setNickname(e.target.value)}
            fullWidth
            required
          />
        </Grid>
        <Grid item xs={12} style={{ width: "100%" }}>
          <Button fullWidth variant="contained" color="secondary" type="submit">
            닉네임 설정하기
          </Button>
        </Grid>
      </Grid>
    </form>
  );
};

export default RegNickname;
