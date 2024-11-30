import java.util.ArrayList;
import java.util.Collections;

import javax.swing.SwingUtilities;

public class GameController {
    private long startTime; // 게임 시작 시간
    private long totalTime; // 총 플레이 시간
    private static ArrayList<Long> rankings = new ArrayList<>(); // 순위 저장

    public void startGame() {
        startTime = System.currentTimeMillis(); // 시작 시간 기록
        totalTime = 0; // 총 시간 초기화
        launchGame1();
    }

    private void launchGame1() {
        SwingUtilities.invokeLater(() -> new Game1(this).start());
    }

    public void gameOver1() {
        launchGame2(); // Game2 시작
    }

    private void launchGame2() {
        SwingUtilities.invokeLater(() -> new Game2(this).start());
    }

    public void gameOver2() {
        launchGame3(); // Game3 시작
    }

    private void launchGame3() {
        SwingUtilities.invokeLater(() -> new Game3(this).start());
    }

    public void gameOver3() {
        totalTime = (System.currentTimeMillis() - startTime) / 1000; // 총 시간 계산
        System.out.println("게임 종료! 총 시간: " + totalTime + "초");

        rankings.add(totalTime); // 총 시간 추가
        rankings.sort(Collections.reverseOrder()); // 내림차순 정렬

        // 상위 10개만 유지
        if (rankings.size() > 10) {
            rankings = new ArrayList<>(rankings.subList(0, 10));
        }

        // 디버깅용 출력
        System.out.println("Ranking list: " + rankings);

        new ResultScreen(this, totalTime); // 결과 화면 호출
    }

    public ArrayList<String> getFormattedRankings() {
        ArrayList<String> formattedRankings = new ArrayList<>();
        for (int i = 0; i < rankings.size(); i++) {
            formattedRankings.add((i + 1) + "위: " + rankings.get(i) + "초");
        }

        // 디버깅용 출력
        if (formattedRankings.isEmpty()) {
            System.out.println("랭킹 데이터가 비어 있습니다.");
        } else {
            System.out.println("현재 랭킹: " + formattedRankings);
        }
        return formattedRankings;
    }
}
