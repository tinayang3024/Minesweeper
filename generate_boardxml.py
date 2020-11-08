import sys

MAX_ROW_COL = 1000

def generate_boardxml(row, col):
    # Fixed info
    info = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n\
<LinearLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n\
    xmlns:app=\"http://schemas.android.com/apk/res-auto\"\n\
    xmlns:tools=\"http://schemas.android.com/tools\"\n\
    android:layout_width=\"match_parent\"\n\
    android:layout_height=\"match_parent\"\n\
    android:orientation=\"vertical\"\n\
    tools:context=\"com.codinginflow.tictactoe.MainActivity\">\n\
    <RelativeLayout\n\
        android:layout_width=\"match_parent\"\n\
        android:layout_height=\"wrap_content\">\n\
        <TextView\n\
            android:id=\"@+id/text_view_timer\"\n\
            android:layout_width=\"wrap_content\"\n\
            android:layout_height=\"wrap_content\"\n\
            android:textSize=\"30sp\" />\n\
        <TextView\n\
            android:id=\"@+id/text_view_remaining_mines\"\n\
            android:layout_width=\"wrap_content\"\n\
            android:layout_height=\"wrap_content\"\n\
            android:layout_below=\"@+id/text_view_timer\"\n\
            android:textSize=\"30sp\" />\n\
        <Button\n\
            android:id=\"@+id/button_reset\"\n\
            android:layout_width=\"wrap_content\"\n\
            android:layout_height=\"wrap_content\"\n\
            android:layout_alignParentEnd=\"true\"\n\
            android:layout_centerVertical=\"true\"\n\
            android:layout_marginEnd=\"33dp\"\n\
            android:text=\"reset\" />\n\
    </RelativeLayout>\n"
    # Info generated based on row and col
    for i in range(row):
        info += "    <LinearLayout\n\
        android:layout_width=\"match_parent\"\n\
        android:layout_height=\"0dp\"\n\
        android:layout_weight=\"1\">\n"
        for j in range(col):
            info += "        <Button\n\
            android:id=\"@+id/button_%03d%03d\"\n\
            android:layout_width=\"0dp\"\n\
            android:layout_height=\"match_parent\"\n\
            android:layout_weight=\"1\"\n\
            android:textSize=\"60sp\"\n\
            android:contentDescription=\"gameboard\"/>\n" % (i,j)
        info += "   </LinearLayout>\n"
    info += "</LinearLayout>"
    return info


def main():
    # Get command line arguments and error checking
    if len(sys.argv) < 3:
        raise ValueError("Error: row or col is not provided")
    row = int(sys.argv[1])
    col = int(sys.argv[2])
    if row > MAX_ROW_COL or col > MAX_ROW_COL:
        raise ValueError("Error: row or col is above maximum constrant")

    # Write the file
    Filename = "app/src/main/res/layout/fragment_second_temp.xml"
    f = open(Filename, 'w')

    f.write(generate_boardxml(row,col))
    f.write('\n')

    f.close()

main()