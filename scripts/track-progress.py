import os
import re
from pathlib import Path
from datetime import datetime

def is_solved(file_path):
    """Check if Solution. kt is implemented (not empty template)"""
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        # Remove comments
        code = re. sub(r'//.*', '', content)
        code = re.sub(r'/\*.*?\*/', '', code, flags=re.DOTALL)
        
        # Remove whitespace
        code_stripped = code. strip()
        
        # Normalize whitespace for comparison
        normalized = re.sub(r'\s+', '', code_stripped)
        
        if normalized == 'classSolution{}':
            return False
        
        # Check if has real implementation (more than 5 non-empty lines)
        lines = [line for line in code.split('\n') if line.strip() and not line.strip().startswith('//')]
        return len(lines) > 5
        
    except Exception as e:
        print(f"Error reading {file_path}: {e}")
        return False

def get_problem_info(problem_dir):
    """Extract problem number and name from directory"""
    dir_name = problem_dir.name
    match = re.match(r'(\d+)-(.+)', dir_name)
    if match:
        return match.group(1), match.group(2)
    return None, None

def scan_problems():
    """Scan all problems and count solved"""
    problems_dir = Path('problems')
    
    stats = {
        'total': 0,
        'solved': 0,
        'by_topic': {},
        'by_difficulty': {'Easy':  0, 'Medium': 0, 'Hard': 0},
        'solved_problems':  []
    }
    
    topic_mapping = {
        'array-string': 'Array/String',
        'two-pointers': 'Two Pointers',
        'sliding-window':  'Sliding Window',
        'matrix': 'Matrix',
        'hashmap': 'Hashmap',
        'intervals':  'Intervals',
        'stack': 'Stack',
        'linked-list': 'Linked List',
        'binary-tree-general': 'Binary Tree General',
        'binary-tree-bfs': 'Binary Tree BFS',
        'binary-search-tree': 'Binary Search Tree',
        'graph-general': 'Graph General',
        'graph-bfs': 'Graph BFS',
        'trie': 'Trie',
        'backtracking': 'Backtracking',
        'divide-and-conquer':  'Divide and Conquer',
        'kadane-algorithm': 'Kadane\'s Algorithm',
        'binary-search': 'Binary Search',
        'heap': 'Heap',
        'bit-manipulation':  'Bit Manipulation',
        'math': 'Math',
        '1d-dp':  '1D Dynamic Programming',
        'multidimensional-dp':  'Multidimensional DP'
    }
    
    for topic_dir in sorted(problems_dir.iterdir()):
        if not topic_dir. is_dir():
            continue
        
        topic_name = topic_mapping.get(topic_dir.name, topic_dir.name.replace('-', ' ').title())
        stats['by_topic'][topic_name] = {
            'total': 0, 
            'solved': 0,
            'by_difficulty':  {'Easy': 0, 'Medium': 0, 'Hard': 0}
        }
        
        for diff_dir in sorted(topic_dir.iterdir()):
            if not diff_dir.is_dir():
                continue
            
            difficulty = diff_dir.name.title()
            
            for problem_dir in sorted(diff_dir.iterdir()):
                if not problem_dir.is_dir():
                    continue
                
                solution_file = problem_dir / 'Solution.kt'
                if solution_file.exists():
                    stats['total'] += 1
                    stats['by_topic'][topic_name]['total'] += 1
                    
                    if is_solved(solution_file):
                        problem_num, problem_name = get_problem_info(problem_dir)
                        
                        stats['solved'] += 1
                        stats['by_topic'][topic_name]['solved'] += 1
                        stats['by_topic'][topic_name]['by_difficulty'][difficulty] += 1
                        stats['by_difficulty'][difficulty] += 1
                        
                        if problem_num:
                            stats['solved_problems'].append({
                                'number': int(problem_num),
                                'name':  problem_name,
                                'topic': topic_name,
                                'difficulty': difficulty
                            })
    
    return stats

def create_progress_bar(current, total, length=10):
    """Create a text progress bar"""
    if total == 0:
        return '░' * length
    filled = int((current / total) * length)
    return '█' * filled + '░' * (length - filled)

def update_progress_file(stats):
    """Update PROGRESS.md with new stats"""
    completion_rate = (stats['solved'] / stats['total'] * 100) if stats['total'] > 0 else 0
    
    content = f"""# 📊 Detailed Progress Tracking

Last Updated: {datetime.now().strftime('%B %d, %Y at %H:%M UTC')}

## Overall Statistics

- **Total Problems**: {stats['solved']} / {stats['total']}
- **Completion Rate**: {completion_rate:.1f}% 
- **Problems Remaining**: {stats['total'] - stats['solved']}

---

## 📈 Progress by Difficulty

### 🟢 Easy ({stats['by_difficulty']['Easy']} / 39 solved)
Progress: {create_progress_bar(stats['by_difficulty']['Easy'], 39)} {stats['by_difficulty']['Easy']}/39 ({stats['by_difficulty']['Easy']/39*100:.0f}%)

### 🟠 Medium ({stats['by_difficulty']['Medium']} / 94 solved)
Progress: {create_progress_bar(stats['by_difficulty']['Medium'], 94)} {stats['by_difficulty']['Medium']}/94 ({stats['by_difficulty']['Medium']/94*100:.0f}%)

### 🔴 Hard ({stats['by_difficulty']['Hard']} / 20 solved)
Progress: {create_progress_bar(stats['by_difficulty']['Hard'], 20)} {stats['by_difficulty']['Hard']}/20 ({stats['by_difficulty']['Hard']/20*100:.0f}%)

---

## 📚 Progress by Topic

"""
    
    for topic, data in sorted(stats['by_topic'].items()):
        if data['total'] > 0:
            topic_rate = (data['solved'] / data['total'] * 100) if data['total'] > 0 else 0
            content += f"### {topic} ({data['solved']}/{data['total']})\n"
            content += f"Progress: {create_progress_bar(data['solved'], data['total'])} {topic_rate:.0f}%\n"
            
            # Show difficulty breakdown
            if data['solved'] > 0:
                difficulties = []
                if data['by_difficulty']['Easy'] > 0:
                    difficulties.append(f"🟢 {data['by_difficulty']['Easy']} Easy")
                if data['by_difficulty']['Medium'] > 0:
                    difficulties.append(f"🟠 {data['by_difficulty']['Medium']} Medium")
                if data['by_difficulty']['Hard'] > 0:
                    difficulties.append(f"🔴 {data['by_difficulty']['Hard']} Hard")
                content += f"- {' | '.join(difficulties)}\n"
            
            content += "\n"
    
    content += """---

## 🎯 Milestones

"""
    milestones = [
        (1, "First problem solved"),
        (10, "10 problems solved"),
        (25, "25 problems solved"),
        (50, "50 problems solved (1/3 complete)"),
        (75, "75 problems solved (halfway!)"),
        (100, "100 problems solved (2/3 complete)"),
        (125, "125 problems solved"),
        (153, "All 153 problems solved! 🎉")
    ]
    
    for threshold, description in milestones:
        checked = 'x' if stats['solved'] >= threshold else ' '
        content += f"- [{checked}] {description}\n"
    
    # Add recently solved problems
    if stats['solved_problems']: 
        content += "\n---\n\n## 🎉 Recently Solved\n\n"
        # Sort by problem number, show last 10
        recent = sorted(stats['solved_problems'], key=lambda x: x['number'], reverse=True)[:10]
        for problem in recent:
            emoji = {'Easy':  '🟢', 'Medium': '🟠', 'Hard': '🔴'}.get(problem['difficulty'], '')
            content += f"- {emoji} **#{problem['number']}** - {problem['name']. replace('-', ' ').title()} ({problem['topic']})\n"
    
    content += "\n---\n\n## 📝 Notes\n\n"
    
    if stats['solved'] == 0:
        content += "Start solving problems and track your progress automatically! Each time you push a solution, this file will update. 🚀\n"
    elif stats['solved'] < stats['total']:
        content += f"Keep going! You've solved {stats['solved']} problems.  Only {stats['total'] - stats['solved']} more to go!  💪\n"
    else: 
        content += "🎉 Congratulations! You've completed all problems! 🎉\n"
    
    with open('PROGRESS.md', 'w', encoding='utf-8') as f:
        f.write(content)

def update_readme_file(stats):
    """Update README.md with progress statistics"""
    try:
        with open('README.md', 'r', encoding='utf-8') as f:
            readme_content = f.read()
    except Exception as e:
        print(f"Error reading README.md: {e}")
        return
    
    # Calculate percentages
    easy_pct = (stats['by_difficulty']['Easy'] / 39 * 100) if 39 > 0 else 0
    medium_pct = (stats['by_difficulty']['Medium'] / 94 * 100) if 94 > 0 else 0
    hard_pct = (stats['by_difficulty']['Hard'] / 20 * 100) if 20 > 0 else 0
    total_pct = (stats['solved'] / 150 * 100) if 150 > 0 else 0
    
    # Update Progress Tracker table
    progress_table = f"""| Difficulty | Solved | Total | Percentage |
|-----------|--------|-------|------------|
| 🟢 Easy | {stats['by_difficulty']['Easy']} | 50+ | {easy_pct:.0f}% |
| 🟠 Medium | {stats['by_difficulty']['Medium']} | 75+ | {medium_pct:.0f}% |
| 🔴 Hard | {stats['by_difficulty']['Hard']} | 25+ | {hard_pct:.0f}% |
| **📈 Total** | **{stats['solved']}** | **150** | **{total_pct:.0f}%** |"""
    
    # Replace the progress tracker table
    readme_content = re.sub(
        r'\| Difficulty \| Solved \| Total \| Percentage \|.*?\| \*\*📈 Total\*\* \| \*\*\d+\*\* \| \*\*150\*\* \| \*\*\d+%\*\* \|',
        progress_table,
        readme_content,
        flags=re.DOTALL
    )
    
    # Build Topics Overview table
    topic_order = [
        'Array/String', 'Two Pointers', 'Sliding Window', 'Matrix', 'Hashmap',
        'Intervals', 'Stack', 'Linked List', 'Binary Tree General', 'Binary Tree BFS',
        'Binary Search Tree', 'Graph General', 'Graph BFS', 'Trie', 'Backtracking',
        'Divide and Conquer', 'Kadane\'s Algorithm', 'Binary Search', 'Heap',
        'Bit Manipulation', 'Math', '1D Dynamic Programming', 'Multidimensional DP'
    ]
    
    topics_table_lines = []
    for i, topic in enumerate(topic_order, 1):
        if topic in stats['by_topic']:
            data = stats['by_topic'][topic]
            easy = data['by_difficulty']['Easy']
            medium = data['by_difficulty']['Medium']
            hard = data['by_difficulty']['Hard']
            total = data['solved']
            topics_table_lines.append(f"| {i} | {topic} | {easy} | {medium} | {hard} | {total} |")
        else:
            topics_table_lines.append(f"| {i} | {topic} | 0 | 0 | 0 | 0 |")
    
    topics_table = "| # | Topic | Easy | Medium | Hard | Total |\n|---|-------|------|--------|------|-------|\n" + "\n".join(topics_table_lines)
    
    # Replace the topics overview table
    readme_content = re.sub(
        r'\| # \| Topic \| Easy \| Medium \| Hard \| Total \|.*?\| 23 \| Multidimensional DP \| \d+ \| \d+ \| \d+ \| \d+ \|',
        topics_table,
        readme_content,
        flags=re.DOTALL
    )
    
    # Write updated content back to README.md
    with open('README.md', 'w', encoding='utf-8') as f:
        f.write(readme_content)

if __name__ == '__main__': 
    print("🔍 Scanning problems...")
    stats = scan_problems()
    print(f"✅ Found {stats['solved']}/{stats['total']} solved problems")
    
    print("📝 Updating PROGRESS.md...")
    update_progress_file(stats)
    print("✅ PROGRESS.md updated successfully!")
    
    print("📝 Updating README.md...")
    update_readme_file(stats)
    print("✅ README.md updated successfully!")
