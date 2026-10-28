import UIKit
import shared

class ComposeViewController: UIViewController {
    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = UIColor.systemBackground
        
        let composeVC = IOSAppContentKt.IOSAppContentBridge()
        composeVC.view.translatesAutoresizingMaskIntoConstraints = false
        view.addSubview(composeVC.view)
        addChild(composeVC)
        
        NSLayoutConstraint.activate([
            composeVC.view.topAnchor.constraint(equalTo: view.topAnchor),
            composeVC.view.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            composeVC.view.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            composeVC.view.bottomAnchor.constraint(equalTo: view.bottomAnchor)
        ])
        
        composeVC.didMove(toParent: self)
    }
}
