const pageRedirect = (url) => {
  try {
    window.location.href = url;
  } catch (error) {
    console.error("page redirect error:", error);
  }
};

const fetchData = async (url) => {
  try {
    console.log("### fetch data", url);
    const response = await fetch(url, {
      method: "GET",
      credentials: "include", // 쿠키를 포함하여 요청 전송
      mode: "cors", // 추가
      headers: {
        "Content-Type": "application/json" // 필요한 경우
      }
    });
    if (!response.ok) {
      if (response.status === 401) {
        // 401 Unauthorized 시 로그인 페이지로 리다이렉트
        console.warn("Unauthorized access detected, redirecting to /login");
        window.location.href = "/pages/login/login3";
        return; // 이후 코드 실행 방지
      }
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    return await response.json();
  } catch (error) {
    console.error("API fetch error:", error);
    throw error;
  }
};

// 추가된 postData 함수
const postData = async (url, data) => {
  try {
    console.log("### post data", url, data);
    const response = await fetch(url, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(data),
      credentials: "include" // 쿠키를 포함하여 요청 전송
    });
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    return await response.json();
  } catch (error) {
    console.error("API post error:", error);
    throw error;
  }
};

const patchData = async (url, data) => {
  try {
    console.log("### patch data", url, data);
    const response = await fetch(url, {
      method: "PATCH",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(data),
      credentials: "include" // 쿠키를 포함하여 요청 전송
    });
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    return await response.json();
  } catch (error) {
    console.error("API post error:", error);
    throw error;
  }
};

const deleteData = async (url, data) => {
  try {
    console.log("### delete data", url, data);
    const response = await fetch(url, {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(data),
      credentials: "include" // 쿠키를 포함하여 요청 전송
    });
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    return await response.json();
  } catch (error) {
    console.error("API post error:", error);
    throw error;
  }
};

export const apiRequest = {
  pageRedirect,
  fetchData,
  postData,
  patchData,
  deleteData
};
